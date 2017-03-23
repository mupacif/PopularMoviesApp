package com.example.android.movies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by student on 21-03-17.
 */

public class MovieContract {


    public static final String AUTHORITY = "com.example.android.movies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "tasks";

    public static final class MovieEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";
        public static final String COL_ID = "id";

        public static final String COL_POSTERPATH= "posterpath";
        public static final String  COL_OVERVIEW = "overview";
        public static final String COL_RELEASEDATE="releaseDate";
        public static final String COL_ORIGINALTITLE="originalTitle";
        public static final String  COL_TITLE = "title";
        public static final String COL_BACKDROPPATH="backdropPath";
//        public static final String  COL_POPULARITY = "popularity";
//        public static final String COL_VOTECOUNT="voteCount";

        public static final String  COL_VOTEAVERAGE = "voteAverage";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ID + " INTEGER NOT NULL,"
                +COL_POSTERPATH+" TEXT,"
        +COL_OVERVIEW+" TEXT,"
                +COL_RELEASEDATE+" TEXT,"
        +COL_ORIGINALTITLE+" TEXT,"
                +COL_TITLE+" TEXT,"
        +COL_BACKDROPPATH+" TEXT,"
//                +COL_POPULARITY+" REAL,"
//       + COL_VOTECOUNT+" REAL,"
        +COL_VOTEAVERAGE+" REAL"
                + ")";


        public static final String UPGRADE_TABLE = "DROP TABLE " + TABLE_NAME;


    }
}
