package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.time.ZoneId;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    public static ZoneId ZONE;

    /**
     * update all habits with new time
     */
    public static void updateTime() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZONE = TimeZone.getDefault().toZoneId();
    }

}