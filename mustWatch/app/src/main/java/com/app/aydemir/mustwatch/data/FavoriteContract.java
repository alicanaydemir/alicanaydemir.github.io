package com.app.aydemir.mustwatch.data;

import android.provider.BaseColumns;

public class FavoriteContract {
    public static final class WatchedEntry implements BaseColumns{
        public static final String TABLE_NAME="watched";
        public static final String COLUMN_MOVIEID="movie_id";
    }
    public static final class ToWatchEntry implements BaseColumns{
        public static final String TABLE_NAME="to_watch";
        public static final String COLUMN_MOVIEID="movie_id";
    }
    public static final class ToWatchDataEntry implements BaseColumns{
        public static final String TABLE_NAME="to_watch_data";
        public static final String ID="id";
        public static final String COLUMN_MOVIEID="movie_id";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_POSTERPATH="poster_path";
        public static final String COLUMN_RELEASEDATE="release_date";
        public static final String COLUMN_PLOT="overview";
        public static final String COLUMN_VOTE="vote";
    }
    public static final class WatchedDataEntry implements BaseColumns{
        public static final String TABLE_NAME="watched_data";
        public static final String ID="id";
        public static final String COLUMN_MOVIEID="movie_id";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_POSTERPATH="poster_path";
        public static final String COLUMN_RELEASEDATE="release_date";
        public static final String COLUMN_PLOT="overview";
        public static final String COLUMN_VOTE="vote";
    }
}
