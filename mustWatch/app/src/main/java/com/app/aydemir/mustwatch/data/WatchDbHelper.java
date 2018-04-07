package com.app.aydemir.mustwatch.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.aydemir.mustwatch.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class WatchDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "watching.db";
    private static final int DATABASE_VERSİON = 1;

    public WatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSİON);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORİTE_TABLE = "CREATE TABLE " + FavoriteContract.ToWatchDataEntry.TABLE_NAME + " (" +
                FavoriteContract.ToWatchDataEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteContract.ToWatchDataEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteContract.ToWatchDataEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.ToWatchDataEntry.COLUMN_POSTERPATH + " TEXT NOT NULL, " +
                FavoriteContract.ToWatchDataEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL, " +
                FavoriteContract.ToWatchDataEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                FavoriteContract.ToWatchDataEntry.COLUMN_VOTE + " TEXT NOT NULL " +
                ");";
        final String SQL_CREATE_FAVORİTE_TABLE2 = "CREATE TABLE " + FavoriteContract.WatchedDataEntry.TABLE_NAME + " (" +
                FavoriteContract.WatchedDataEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteContract.WatchedDataEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.WatchedDataEntry.COLUMN_POSTERPATH + " TEXT NOT NULL, " +
                FavoriteContract.WatchedDataEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL, " +
                FavoriteContract.WatchedDataEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                FavoriteContract.WatchedDataEntry.COLUMN_VOTE + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORİTE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORİTE_TABLE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.ToWatchEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void add_towatch_data(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.ToWatchDataEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteContract.ToWatchDataEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteContract.ToWatchDataEntry.COLUMN_POSTERPATH, movie.getPosterPath2());
        values.put(FavoriteContract.ToWatchDataEntry.COLUMN_RELEASEDATE, movie.getReleaseDate());
        values.put(FavoriteContract.ToWatchDataEntry.COLUMN_PLOT, movie.getOverview());
        values.put(FavoriteContract.ToWatchDataEntry.COLUMN_VOTE, movie.getVoteAverage());
        db.insert(FavoriteContract.ToWatchDataEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void delete_towatch_data(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.ToWatchDataEntry.TABLE_NAME, FavoriteContract.ToWatchDataEntry.COLUMN_MOVIEID + "=" + id, null);
        db.close();
    }


    public void add_watched_data(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteContract.WatchedDataEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteContract.WatchedDataEntry.COLUMN_POSTERPATH, movie.getPosterPath2());
        values.put(FavoriteContract.WatchedDataEntry.COLUMN_RELEASEDATE, movie.getReleaseDate());
        values.put(FavoriteContract.WatchedDataEntry.COLUMN_PLOT, movie.getOverview());
        values.put(FavoriteContract.WatchedDataEntry.COLUMN_VOTE, movie.getVoteAverage());
        db.insert(FavoriteContract.WatchedDataEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void delete_watched_data(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.WatchedDataEntry.TABLE_NAME, FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID + "=" + id, null);

        db.close();

    }

    public List<Movie> getToWatchData(int page_number,int item_count) {
        String[] columns = {
                FavoriteContract.ToWatchDataEntry.ID,
                FavoriteContract.ToWatchDataEntry.COLUMN_MOVIEID,
                FavoriteContract.ToWatchDataEntry.COLUMN_TITLE,
                FavoriteContract.ToWatchDataEntry.COLUMN_POSTERPATH,
                FavoriteContract.ToWatchDataEntry.COLUMN_RELEASEDATE,
                FavoriteContract.ToWatchDataEntry.COLUMN_PLOT,
                FavoriteContract.ToWatchDataEntry.COLUMN_VOTE
        };
        String sortOrder = FavoriteContract.ToWatchDataEntry.ID + " DESC "+" LIMIT "+item_count+" OFFSET "+(page_number-1)*item_count;
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(FavoriteContract.ToWatchDataEntry.TABLE_NAME, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteContract.ToWatchDataEntry.COLUMN_MOVIEID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.ToWatchDataEntry.COLUMN_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.ToWatchDataEntry.COLUMN_POSTERPATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.ToWatchDataEntry.COLUMN_PLOT)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.ToWatchDataEntry.COLUMN_RELEASEDATE)));
                movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteContract.ToWatchDataEntry.COLUMN_VOTE))));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return movieList;
    }

    public List<Movie> getWatchedData(int page_number,int item_count) {
        String[] columns = {
                FavoriteContract.WatchedDataEntry.ID,
                FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID,
                FavoriteContract.WatchedDataEntry.COLUMN_TITLE,
                FavoriteContract.WatchedDataEntry.COLUMN_POSTERPATH,
                FavoriteContract.WatchedDataEntry.COLUMN_RELEASEDATE,
                FavoriteContract.WatchedDataEntry.COLUMN_PLOT,
                FavoriteContract.WatchedDataEntry.COLUMN_VOTE
        };
        String sortOrder = FavoriteContract.WatchedDataEntry.ID + " DESC "+" LIMIT "+item_count+" OFFSET "+(page_number-1)*item_count;
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(FavoriteContract.WatchedDataEntry.TABLE_NAME, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_POSTERPATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_PLOT)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_RELEASEDATE)));
                movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_VOTE))));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return movieList;
    }

    /*public List<Movie> getSearchData() {
        String[] columns = {
                FavoriteContract.WatchedDataEntry.ID,
                FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID,
                FavoriteContract.WatchedDataEntry.COLUMN_TITLE,
                FavoriteContract.WatchedDataEntry.COLUMN_POSTERPATH,
                FavoriteContract.WatchedDataEntry.COLUMN_RELEASEDATE,
                FavoriteContract.WatchedDataEntry.COLUMN_PLOT,
                FavoriteContract.WatchedDataEntry.COLUMN_VOTE
        };
        String sortOrder = FavoriteContract.WatchedDataEntry.ID + " DESC ";
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(FavoriteContract.WatchedDataEntry.TABLE_NAME, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_MOVIEID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_POSTERPATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_PLOT)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_RELEASEDATE)));
                movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteContract.WatchedDataEntry.COLUMN_VOTE))));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return movieList;
    }*/
}

