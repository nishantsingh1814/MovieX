package com.eventx.moviex.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;
import static android.R.attr.rating;
import static android.R.attr.version;

/**
 * Created by Nishant on 4/7/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "moviex_wishlist";
    public static final String MOVIE_WISHLIST_TABLE = "movie_wishlist";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MOVIE_TITLE = "title";
    public static final String COLUMN_MOVIE_POSTER = "poster";

    public static final String TV_WISHLIST_TABLE = "tv_wishlist";
    public static final String COLUMN_TV_ID = "tv_id";
    public static final String COLUMN_TV_TITLE = "title";
    public static final String COLUMN_TV_POSTER = "poster";

    public static final String EPISODE_WATCHED_TABLE = "epidoe_watched";
    public static final String COLUMN_EPISODE_ID = "episode_id";

    public static final String MOVIE_RATED_TABLE = "movie_rates";
    public static final String COLUMN_MOVIE_RATED_ID = "movie_id";
    public static final String COLUMN_RATING = "rating";

    public static final String TV_RATED_TABLE = "tv_rates";
    public static final String COLUMN_TV_RATED_ID = "tv_id";

    public static final String TV_EPISODE_RATED_TABLE = "tv_episode_rates";
    public static final String COLUMN_TV_EPISODE_RATED_ID = "tv_episode_id";

    public static final String MOVIE_WATCH_TABLE = "movie_watchlist";
    public static final String COLUMN_MOVIE_WATCHLIST_ID = "movie_id";

    public static final String TV_WATCH_TABLE = "tv_watchlist";
    public static final String COLUMN_TV_WATCHLIST_ID = "tv_id";

    public static final String MOVIE_FAVOURITE_TABLE = "movie_favourite";
    public static final String COLUMN_MOVIE_FAVOURITE_ID = "movie_id";

    public static final String TV_FAVOURITE_TABLE = "tv_favourite";
    public static final String COLUMN_TV_FAVOURITE_ID = "tv_id";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + MOVIE_WISHLIST_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVIE_ID + " INTEGER, " + COLUMN_MOVIE_POSTER + " TEXT, " + COLUMN_MOVIE_TITLE + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TV_WISHLIST_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TV_ID + " INTEGER, " + COLUMN_TV_POSTER + " TEXT, " + COLUMN_TV_TITLE + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + EPISODE_WATCHED_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EPISODE_ID + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + MOVIE_RATED_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVIE_RATED_ID + " INTEGER ," + COLUMN_RATING + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TV_RATED_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TV_RATED_ID + " INTEGER ," + COLUMN_RATING + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TV_EPISODE_RATED_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TV_EPISODE_RATED_ID + " INTEGER ," + COLUMN_RATING + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + MOVIE_WATCH_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVIE_WATCHLIST_ID + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TV_WATCH_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TV_WATCHLIST_ID + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + MOVIE_FAVOURITE_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVIE_FAVOURITE_ID + " INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TV_FAVOURITE_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TV_FAVOURITE_ID + " INTEGER )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addToSqlite(SQLiteDatabase db, String tableName, long id, String title, String poster) {
        ContentValues values = new ContentValues();
        if (tableName.equals(MOVIE_WISHLIST_TABLE)) {
            values.put(COLUMN_MOVIE_ID, id);
            values.put(COLUMN_MOVIE_TITLE, title);
            values.put(COLUMN_MOVIE_POSTER, poster);
            db.insert(tableName, null, values);
        } else if (tableName.equals(TV_WISHLIST_TABLE)) {
            values.put(COLUMN_TV_ID, id);
            values.put(COLUMN_TV_TITLE, title);
            values.put(COLUMN_TV_POSTER, poster);
            db.insert(tableName, null, values);
        }


    }

    public void addRating(SQLiteDatabase db, String tableName, long id, float rating) {
        ContentValues values = new ContentValues();

        if (tableName.equals(MOVIE_RATED_TABLE)) {
            values.put(COLUMN_MOVIE_RATED_ID, id);
            values.put(COLUMN_RATING, rating);
            db.insert(tableName, null, values);
        } else if (tableName.equals(TV_RATED_TABLE)) {
            values.put(COLUMN_TV_ID, id);
            values.put(COLUMN_RATING, rating);
            db.insert(tableName, null, values);
        } else if (tableName.equals(TV_EPISODE_RATED_TABLE)) {
            values.put(COLUMN_TV_EPISODE_RATED_ID, id);
            values.put(COLUMN_RATING, rating);
            db.insert(tableName, null, values);
        }

    }

    public void updateRating(SQLiteDatabase db, String tableName, long id, float rating) {
        ContentValues values = new ContentValues();

        if (tableName.equals(MOVIE_RATED_TABLE)) {
            values.put(COLUMN_MOVIE_RATED_ID, id);
            values.put(COLUMN_RATING, rating);
            db.update(tableName, values, COLUMN_MOVIE_RATED_ID + " =" + id, null);
        } else if (tableName.equals(TV_RATED_TABLE)) {
            values.put(COLUMN_TV_ID, id);
            values.put(COLUMN_RATING, rating);
            db.update(tableName, values, COLUMN_TV_RATED_ID + " =" + id, null);
        } else if (tableName.equals(TV_EPISODE_RATED_TABLE)) {
            values.put(COLUMN_TV_EPISODE_RATED_ID, id);
            values.put(COLUMN_RATING, rating);
            db.update(tableName, values, COLUMN_TV_EPISODE_RATED_ID + " =" + id, null);
        }
    }

    public void addToEpisode(SQLiteDatabase db, String tableName, long id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EPISODE_ID, id);
        db.insert(tableName, null, values);
    }

    public void addToWatchlist(SQLiteDatabase db, String tableName, long id) {
        ContentValues values = new ContentValues();
        if (tableName.equals(MOVIE_WATCH_TABLE)) {
            values.put(COLUMN_MOVIE_WATCHLIST_ID, id);
            db.insert(tableName, null, values);
        } else if (tableName.equals(TV_WATCH_TABLE)) {
            values.put(COLUMN_TV_WATCHLIST_ID, id);
            db.insert(tableName, null, values);
        }
    }
    public void addToFavourite(SQLiteDatabase db, String tableName, long id) {
        ContentValues values = new ContentValues();
        if (tableName.equals(MOVIE_FAVOURITE_TABLE)) {
            values.put(COLUMN_MOVIE_FAVOURITE_ID, id);
            db.insert(tableName, null, values);
        } else if (tableName.equals(TV_FAVOURITE_TABLE)) {
            values.put(COLUMN_TV_FAVOURITE_ID, id);
            db.insert(tableName, null, values);
        }
    }
}
