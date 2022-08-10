package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentFinishListener {

    public static ZoneId ZONE;
    public static String HABITDIR = "habit.txt";

    protected RecyclerView recyclerView;
    protected HabitAdapter habitAdapter;
    protected LinearLayoutManager layoutManager;
    protected ArrayList<Habit> data;

    private Handler handler;
    private final int delay = 5000; // 5 second delay, see if this is too often/not often enough

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZONE = TimeZone.getDefault().toZoneId();

        // todo: maybe use savedInstanceState for smoothness
        data = new ArrayList<Habit>();
        initData(data);

        recyclerView = findViewById(R.id.main_recyclerview_habitlist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        habitAdapter = new HabitAdapter(data);
        /*
        // from https://stackoverflow.com/a/32488059
        // look also at HabitAdapter, where some unique stable habit id needs to exist
        // without this, listview blinks every [delay] seconds; may also be performance issue
        habitAdapter.setHasStableIds(true);
         */
        recyclerView.setAdapter(habitAdapter);

        // below updates habits every [delay] seconds
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                habitAdapter.update(layoutManager.findFirstVisibleItemPosition(),
                        layoutManager.findLastVisibleItemPosition());
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(data);
        // inits ENTIRE data; maybe not performant with lots of habits
        habitAdapter.notifyDataSetChanged();
    }

    // probably doesn't need habits param
    private void saveData(ArrayList<Habit> habits) {
        try {
            // redundant, but i need to create the file first
            File file = new File(getFilesDir(), HABITDIR);
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter
                    = new OutputStreamWriter(stream);
            for (Habit habit : habits) {
                String entry = habit.getName() + "/" +
                        habit.getStartTimeMillis() + "/" +
                        habit.getLastDoneMillis() + "/" +
                        habit.getInterval() + "\n";

                outputStreamWriter.write(entry);
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("write IOException", "Failed to write habits: ", e);
        }
    }

    private void initData(ArrayList<Habit> habits) {
        try {
            data.clear();
            File file = new File(getFilesDir(), HABITDIR);
            FileInputStream fileInputStream = new FileInputStream(file);
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    /*
                     * 0 is name string
                     * 1 is starting time in millis since epoch
                     * 2 is last done time in millis since epoch
                     * 3 is interval millis
                     */
                    String[] habitData = line.split("/",0);

                    if (habitData.length == 4) {
                        Habit habitToAdd = new Habit(habitData[0], Long.parseLong(habitData[1]),
                                Long.parseLong(habitData[2]), Long.parseLong(habitData[3]));
                        data.add(habitToAdd);
                    } else { // data wrongly unpacked; less/more than 4 elements
                        Log.e("Bad Unpack", "Unpacked data doesn't have 4 elements");
                    }
                }
                bufferedReader.close();
                inputStreamReader.close();
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("read FileNotFoundException", "File not found: ", e);
        } catch (IOException e) {
            Log.e("read IOException", "File not readable: ", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_addhabit:
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentFinish(Habit newHabit) {
        data.add(newHabit);
        habitAdapter.notifyItemInserted(data.size() - 1);
    }

}