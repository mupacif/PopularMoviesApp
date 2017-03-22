package com.example.android.movies.database;

import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;

/**
 * Created by student on 21-03-17.
 */

public class MovieContract {


    public static final String AUTHORITY  = "com.example.android.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIES = "tasks";

    public static final class MovieEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";
        public static final String COL_IDLIKEDMOVIE = "idMovie";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_IDLIKEDMOVIE + " INTEGER NOT NULL"
                + ")";


        public static final String UPGRADE_TABLE = "DROP TABLE "+TABLE_NAME;



    }
}
