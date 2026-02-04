package com.example.theloop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int VERSION = 1;

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // define table & columns
    public static final class EventTable {
        public static final String TABLE = "events";
        public static final String COL_ID = "_id";
        public static final String COL_ARTIST = "artist";
        public static final String COL_LOCATION = "location";
        public static final String COL_DATE = "date";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EventTable.TABLE + " (" +
                EventTable.COL_ID + " integer primary key autoincrement, " +
                EventTable.COL_ARTIST + " text, " +
                EventTable.COL_LOCATION + " text, " +
                EventTable.COL_DATE + " text)");

        // Test Events - can comment out
        db.execSQL("INSERT INTO " + EventTable.TABLE + " (" +
                EventTable.COL_ARTIST + ", " +
                EventTable.COL_LOCATION + ", " +
                EventTable.COL_DATE + ") VALUES " +
                "(1, 'Enhypen', 'United Center', '2025-08-09'), " +
                "(2, 'Lollapalooza', 'Grant Park', '2025-07-31'), " +
                "(3, 'Fujii Kaze', 'Vic Theater', '2025-08-01')");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + EventTable.TABLE);
        onCreate(db);
    }

    // --- CRUD METHODS ---

    public long addEvent(String artist, String location, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventTable.COL_ARTIST, artist);
        values.put(EventTable.COL_LOCATION, location);
        values.put(EventTable.COL_DATE, date);
        return db.insert(EventTable.TABLE, null, values);
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(EventTable.TABLE,
                null, null, null, null, null,
                EventTable.COL_DATE + " ASC");
    }

    public int deleteEvent(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(EventTable.TABLE,
                EventTable.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int updateEvent(long id, String artist, String location, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventTable.COL_ARTIST, artist);
        values.put(EventTable.COL_LOCATION, location);
        values.put(EventTable.COL_DATE, date);

        return db.update(EventTable.TABLE, values,
                EventTable.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
