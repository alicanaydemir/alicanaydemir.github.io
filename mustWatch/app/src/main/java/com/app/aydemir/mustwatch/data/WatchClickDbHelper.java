package com.app.aydemir.mustwatch.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WatchClickDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSİON = 1;

    public WatchClickDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSİON);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORİTE_TABLE = "CREATE TABLE " + FavoriteContract.ToWatchEntry.TABLE_NAME + " (" +
                FavoriteContract.ToWatchEntry.COLUMN_MOVIEID + " INTEGER PRIMARY KEY "+
                ");";
        final String SQL_CREATE_FAVORİTE_TABLE2 = "CREATE TABLE " + FavoriteContract.WatchedEntry.TABLE_NAME + " (" +
                FavoriteContract.WatchedEntry.COLUMN_MOVIEID + " INTEGER PRIMARY KEY "+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORİTE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORİTE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.ToWatchEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void add_towatch(int movie_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.ToWatchEntry.COLUMN_MOVIEID, movie_id);
        db.insert(FavoriteContract.ToWatchEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void delete_towatch(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.ToWatchEntry.TABLE_NAME, FavoriteContract.ToWatchEntry.COLUMN_MOVIEID + "=" + id, null);
        db.close();
    }
    public void add_watched(int movie_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.WatchedEntry.COLUMN_MOVIEID, movie_id);
        db.insert(FavoriteContract.WatchedEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void delete_watched(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.WatchedEntry.TABLE_NAME, FavoriteContract.WatchedEntry.COLUMN_MOVIEID + "=" + id, null);
        db.close();
    }
}

