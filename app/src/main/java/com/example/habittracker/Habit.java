package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Habit implements Parcelable {

    private String name;
    private ZonedDateTime startTime;
    private ZonedDateTime lastDone;
    private long interval;

    /**
     * construct a Habit by providing attributes
     * @param id the name of the habit
     * @param start what datetime the habit interval starts tracking at
     * @param length length of time before habit status "resets" in milliseconds
     */
    public Habit(String id, ZonedDateTime start, long length) {
    }

    /**
     * construct a Habit from its parceled form
     * longs are unpacked and turned into ZonedDateTime
     * @param in the parcel to unpack
     */
    protected Habit(Parcel in) {
        name = in.readString();
        long startMillis = in.readLong();
        long lastMillis = in.readLong();
        interval = in.readLong();

        startTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startMillis), MainActivity.ZONE);
        lastDone = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastMillis), MainActivity.ZONE);

    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * set time last "marked done" to current time
     */
    public void setDone() {
        lastDone = ZonedDateTime.now();
    }

    /**
     * set time last "marked done" to before current interval
     * this will result in the list item showing up as undone
     */
    public void unsetDone() {
        lastDone = lastDone.minus(interval, ChronoUnit.MILLIS);
    }

    public String getName() {return name;}

    /**
     * get next reset date of this habit
     * @return next reset date in form ZonedDateTime
     */
    public ZonedDateTime getResetDate() {
        // first, find number of intervals to get to next reset
        long startMillis = startTime.toInstant().toEpochMilli();
        long millisSinceStart = ZonedDateTime.now().toInstant().toEpochMilli() - startMillis;
        long intervalsSinceStart = millisSinceStart / interval;
        // now current "interval count" is known; can find next reset date by adding one interval
        return startTime.plus(interval * (intervalsSinceStart + 1), ChronoUnit.MILLIS);
    }

    /**
     * pack a Habit into a parcel
     * note the order of packed longs
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(startTime.toInstant().toEpochMilli());
        parcel.writeLong(lastDone.toInstant().toEpochMilli());
        parcel.writeLong(interval);
    }
}
