package com.rahuljanagouda.popularmoviestwo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;


public class MoviesOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MOVIES_DB";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE =
            "create table " + MoviesContract.MoviesEntry.TABLE_MOVIES + " ("
                    + MoviesContract.MoviesEntry.ID + " integer primary key autoincrement, "
                    + MoviesContract.MoviesEntry.MOVIE_ID + " integer , "
                    + MoviesContract.MoviesEntry.MOVIE_ADULT + " integer default 0 , "
                    + MoviesContract.MoviesEntry.MOVIE_POSTER_PATH + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_OVERVIEW + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_GENRES + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_TITLE + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_ORIGINAL_TITLE + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_LANGUAGE + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_BACKDROP_PATH + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_POPULARITY + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_VIDEO + " integer default 0 , "
                    + MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE + " text , "
                    + MoviesContract.MoviesEntry.MOVIE_VOTE_COUNT + " integer ) ; ";

    public MoviesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @NonNull
    public static ContentValues getMovieContentValues(@NonNull Result movie) {

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.MOVIE_ID, movie.getId());
        values.put(MoviesContract.MoviesEntry.MOVIE_ADULT, movie.getAdult() ? 1 : 0);
        values.put(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MoviesContract.MoviesEntry.MOVIE_OVERVIEW, movie.getOverview());

        String genres = "";
        String prefix = "";

        for (int i = 0; i < movie.getGenreIds().size(); ++i) {
            genres += prefix + movie.getGenreIds().get(i).toString();
            prefix = ",";
        }

        values.put(MoviesContract.MoviesEntry.MOVIE_GENRES, genres);
        values.put(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(MoviesContract.MoviesEntry.MOVIE_TITLE, movie.getTitle());
        values.put(MoviesContract.MoviesEntry.MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MoviesContract.MoviesEntry.MOVIE_LANGUAGE, movie.getOriginalLanguage());
        values.put(MoviesContract.MoviesEntry.MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MoviesContract.MoviesEntry.MOVIE_POPULARITY, movie.getPopularity());
        values.put(MoviesContract.MoviesEntry.MOVIE_VIDEO, movie.getVideo() ? 1 : 0);
        values.put(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MoviesContract.MoviesEntry.MOVIE_VOTE_COUNT, movie.getVoteCount());

        return values;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MoviesOpenHelper", "Upgrading database from version " + oldVersion + " to " + newVersion + ". OLD DATA WILL BE DESTROYED");

        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_MOVIES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MoviesContract.MoviesEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(db);
    }
}
