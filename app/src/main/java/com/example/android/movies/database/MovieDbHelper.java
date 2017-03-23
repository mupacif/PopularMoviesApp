package com.example.android.movies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by student on 21-03-17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Result.db";
    public static final int DB_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieContract.MovieEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MovieContract.MovieEntry.UPGRADE_TABLE);
        onCreate(db);
    }
}
