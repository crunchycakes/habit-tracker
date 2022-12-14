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
     * this constructor used to make new habit
     * @param id the name of the habit
     * @param start what datetime the habit interval starts tracking at in milliseconds since epoch
     * @param length length of time before habit status "resets" in milliseconds since epoch
     */
    public Habit(String id, long start, long length) {
        name = id;
        startTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(start), MainActivity.ZONE);
        interval = length;
        lastDone = startTime.minusSeconds(1);
    }

    /**
     * construct a habit more completely, also including last done time
     * this constructor used to restore habit from storage
     * @param id name of the habit
     * @param start what datetime the habit interval first starts tracking in milliseconds since epoch
     * @param last last datetime the habit was "done" in milliseconds since epoch
     * @param length length of time before habit status "resets" in milliseconds since epoch
     */
    public Habit(String id, long start, long last, long length) {
        name = id;
        startTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(start), MainActivity.ZONE);
        interval = length;
        lastDone = ZonedDateTime.ofInstant(Instant.ofEpochMilli(last), MainActivity.ZONE);
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
     * i.e. the last "marked done" date is turned back by one interval
     * this will result in the list item showing up as undone
     */
    public void unsetDone() {
        lastDone = lastDone.minus(interval, ChronoUnit.MILLIS);
    }

    /**
     * check whether the habit is considered "done" based on last reset date
     * @return boolean, whether habit is considered "done" for the interval
     */
    public boolean isDone() {
        // may be too redundant with getResetDate()
        ZonedDateTime lastReset = startTime
                .plus(interval * (intervalsSinceStart()), ChronoUnit.MILLIS);
        return lastDone.isAfter(lastReset);
    }

    /**
     * get next reset date of this habit
     * @return next reset date in form ZonedDateTime
     */
    public ZonedDateTime getResetDate() {
        long intervalsSinceStart = intervalsSinceStart();
        // now current "interval count" is known; can find next reset date by adding one interval
        return startTime.plus(interval * (intervalsSinceStart + 1), ChronoUnit.MILLIS);
    }

    public String getName() {return name;}
    public long getStartTimeMillis() {return startTime.toInstant().toEpochMilli();}
    public long getLastDoneMillis() {return lastDone.toInstant().toEpochMilli();}
    public long getInterval() {return interval;}

    /**
     * helper to determine how many FULL intervals have passed since start date
     * i.e. the last date when habit reset
     * @return how many intervals have passed since start date
     */
    private long intervalsSinceStart() {
        long startMillis = startTime.toInstant().toEpochMilli();
        long millisSinceStart = ZonedDateTime.now().toInstant().toEpochMilli() - startMillis;
        return millisSinceStart / interval;
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
