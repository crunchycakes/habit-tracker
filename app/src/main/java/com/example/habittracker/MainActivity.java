package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    public static ZoneId ZONE;

    protected RecyclerView recyclerView;
    protected HabitAdapter habitAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Habit> data;

    /**
     * update all habits with current time
     */
    public static void update() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZONE = TimeZone.getDefault().toZoneId();

        // todo: fetch data
        data = new ArrayList<Habit>();
        data.add(new Habit("test habit", ZonedDateTime.now(), (long) 86400000)); // 24 hours

        recyclerView = findViewById(R.id.main_recyclerview_habitlist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        habitAdapter = new HabitAdapter(data);
        recyclerView.setAdapter(habitAdapter);
    }

    // todo: onCreateOptionsMenu along with other menu stuff

}