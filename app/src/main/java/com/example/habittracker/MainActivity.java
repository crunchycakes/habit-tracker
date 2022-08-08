package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    public static ZoneId ZONE;

    protected RecyclerView recyclerView;
    protected HabitAdapter habitAdapter;
    protected LinearLayoutManager layoutManager;
    protected ArrayList<Habit> data;

    private Handler handler;
    private int delay = 5000; // 5 second delay, see if this is too often/not often enough

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZONE = TimeZone.getDefault().toZoneId();

        // todo: fetch data
        data = new ArrayList<Habit>();
        Habit tempHabit = new Habit("test habit", ZonedDateTime.now(), (long) 60000); // 1 minute
        tempHabit.setDone();
        data.add(tempHabit);

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

    // todo: onCreateOptionsMenu along with other menu stuff

}