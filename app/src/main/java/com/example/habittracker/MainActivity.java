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
        recyclerView.setAdapter(habitAdapter);
    }

    // todo: onCreateOptionsMenu along with other menu stuff

}