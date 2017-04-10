package com.eventx.moviex.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by Nishant on 4/7/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="moviex_wishlist";
    public static final String MOVIE_WISHLIST_TABLE="movie_wishlist";
    public static final String COLUMN_MOVIE_ID="movie_id";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_MOVIE_TITLE="title";
    public static final String COLUMN_MOVIE_POSTER="poster";

    public static final String TV_WISHLIST_TABLE="tv_wishlist";
    public static final String COLUMN_TV_ID="tv_id";
    public static final String COLUMN_TV_TITLE="title";
    public static final String COLUMN_TV_POSTER="poster";
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+MOVIE_WISHLIST_TABLE+" ( "+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_MOVIE_ID+" INTEGER, "+COLUMN_MOVIE_POSTER+" TEXT, "+COLUMN_MOVIE_TITLE+" TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE "+TV_WISHLIST_TABLE+" ( "+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_TV_ID+" INTEGER, "+COLUMN_TV_POSTER+" TEXT, "+COLUMN_TV_TITLE+" TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addToSqlite(SQLiteDatabase db,String tableName,long id,String title,String poster){
        ContentValues values=new ContentValues();
        if(tableName.equals(MOVIE_WISHLIST_TABLE)) {
            values.put(COLUMN_MOVIE_ID, id);
            values.put(COLUMN_MOVIE_TITLE, title);
            values.put(COLUMN_MOVIE_POSTER, poster);
            db.insert(tableName, null, values);
        }
        else if(tableName.equals(TV_WISHLIST_TABLE)){
            values.put(COLUMN_TV_ID, id);
            values.put(COLUMN_TV_TITLE, title);
            values.put(COLUMN_TV_POSTER, poster);
            db.insert(tableName, null, values);
        }
    }
}
