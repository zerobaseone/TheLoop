package com.example.theloop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "login.db";
    private static final int VERSION = 1;

    public LoginDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static class LoginTable {
        public static final String TABLE = "logins";
        public static final String COL_ID = "_id";
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LoginTable.TABLE + " (" +
                LoginTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LoginTable.COL_USERNAME + " TEXT UNIQUE, " +
                LoginTable.COL_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LoginTable.TABLE);
        onCreate(db);
    }

    // Add new login
    public long addLogin(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoginTable.COL_USERNAME, username);
        values.put(LoginTable.COL_PASSWORD, password);

        return db.insert(LoginTable.TABLE, null, values);
    }

    // Check if username/password match
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LoginTable.TABLE,
                new String[]{LoginTable.COL_ID},
                LoginTable.COL_USERNAME + "=? AND " + LoginTable.COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Optional: check if username exists
    public boolean usernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LoginTable.TABLE,
                new String[]{LoginTable.COL_ID},
                LoginTable.COL_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
