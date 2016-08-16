package com.rahuljanagouda.popularmoviestwo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by rahuljanagouda on 14/02/16.
 */
public class Network {
    public static final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String TMDB_IMAGE_HQ_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static final String TMDB_API_KEY = "32678d6d18172964c5930ff3e5d3b385";
    private static final String STRING_MOVIE = "movie";
    private static final String STRING_SORT_BY_POPULARITY_VALUE = "popular";
    private static final String STRING_SORT_BY_HIGHEST_RATED_VALUE = "top_rated";
    private static final String URL_TMDB_API_BASE = "http://api.themoviedb.org/3/movie/";
    private static final String URL_TMDB_DISCOVER_MOVIES_POPULAR = URL_TMDB_API_BASE + STRING_SORT_BY_POPULARITY_VALUE;
    public static final String URL_TMDB_DISCOVER_MOVIES_POPULAR_API = URL_TMDB_DISCOVER_MOVIES_POPULAR + "?" +
            "api_key=" + TMDB_API_KEY;
    private static final String URL_TMDB_DISCOVER_MOVIES_HIGHEST_RATED = URL_TMDB_API_BASE + STRING_SORT_BY_HIGHEST_RATED_VALUE;
    public static final String URL_TMDB_DISCOVER_MOVIES_HIGHEST_RATED_API = URL_TMDB_DISCOVER_MOVIES_HIGHEST_RATED + "?" +
            "api_key=" + TMDB_API_KEY;


    public static final String TMDB_VIDEO_URL = URL_TMDB_API_BASE + "{id}" + "/videos" + "?" +
            "api_key=" + TMDB_API_KEY;

    public static final String TMDB_REVIEW_URL = URL_TMDB_API_BASE + "{id}" + "/reviews" + "?" +
            "api_key=" + TMDB_API_KEY;

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static void watchYoutubeVideo(Context mContext, String id) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        mContext.startActivity(intent);
    }
}
