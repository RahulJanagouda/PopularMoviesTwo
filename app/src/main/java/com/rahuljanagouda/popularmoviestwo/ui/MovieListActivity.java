package com.rahuljanagouda.popularmoviestwo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rahuljanagouda.popularmoviestwo.AppController;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.adapters.GridSpacingItemDecoration;
import com.rahuljanagouda.popularmoviestwo.adapters.MovieListRecyclerAdapter;
import com.rahuljanagouda.popularmoviestwo.customViews.MaterialProgressDialog;
import com.rahuljanagouda.popularmoviestwo.database.DatabaseChangedReceiver;
import com.rahuljanagouda.popularmoviestwo.database.MoviesContract;
import com.rahuljanagouda.popularmoviestwo.helper.GsonRequest;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.MovieApiResponse;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

import java.util.ArrayList;
import java.util.List;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    private static final int FAVORITE_SORT_ORDER = 2;
    private static final int DELAY_MILLIS = 50;
    private static int sortByOption;
    @NonNull
    private final Context mContext = this;
    @NonNull
    private final DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
        public void onReceive(Context context, Intent intent) {
            getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, (MovieListActivity) mContext);
        }
    };
    private Loader<Cursor> loaderManager;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView moviesGrid;
    @Nullable
    private MovieApiResponse movieApiResponse = null;
    private LinearLayout errorSection;
    private MaterialProgressDialog materialProgressDialog;
    private TextView noFavorites;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


//      Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

//      Set the shared Preferences and default/last sort option
        sharedPref = mContext.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sortByOption = sharedPref.getInt(getString(R.string.last_sort_order), 0);


//        Set the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Sort By")
                        .setItems(R.array.sort_by, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                makeNetworkRequest(which);

                            }
                        });
                builder.create().show();
            }
        });

//      Find Views
        noFavorites = (TextView) findViewById(R.id.noFavorites);
        moviesGrid = (RecyclerView) findViewById(R.id.movie_list);
        errorSection = (LinearLayout) findViewById(R.id.errorSection);

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//        }


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        materialProgressDialog = MaterialProgressDialog.show(mContext, "Listing movies...", false, true);
        if (savedInstanceState == null || !savedInstanceState.containsKey("MovieApiResponse")) {
            checkInternetAndRequestData(sortByOption);
        } else {
            movieApiResponse = savedInstanceState.getParcelable("MovieApiResponse");
            initAdapter(moviesGrid, movieApiResponse);
        }


//        materialProgressDialog.;

    }


    private void initAdapter(@NonNull final RecyclerView recyclerView, MovieApiResponse response) {
        showNoFavorite(false);

        recyclerView.setAdapter(new MovieListRecyclerAdapter(mContext, response, mTwoPane));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(FAVORITE_SORT_ORDER, 3, true));
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, FAVORITE_SORT_ORDER));


        if (mTwoPane) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();
                }
            }, DELAY_MILLIS);
        }

        materialProgressDialog.dismiss();

    }

    private void showNoFavorite(boolean value) {

        int noFavoriteVisibility = value ? View.VISIBLE : View.GONE;
        noFavorites.setVisibility(noFavoriteVisibility);

        int recyclerViewVisibility = value ? View.GONE : View.VISIBLE;
        moviesGrid.setVisibility(recyclerViewVisibility);
    }


    private void checkInternetAndRequestData(final int sortByOption) {
        if (Network.isOnline(mContext)) {
            makeNetworkRequest(sortByOption);
        } else {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Oops, No internet connection found. Please connect and retry again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkInternetAndRequestData(sortByOption);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void makeNetworkRequest(int i) {

        String URL;
        sortByOption = i;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.last_sort_order), sortByOption);
        editor.commit();

        switch (i) {
            case 1:
                URL = Network.URL_TMDB_DISCOVER_MOVIES_HIGHEST_RATED_API;
                break;
            case 2:
                loaderManager = getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
                return;
            default:
                URL = Network.URL_TMDB_DISCOVER_MOVIES_POPULAR_API;
        }

        GsonRequest<MovieApiResponse> gsonRequest = new GsonRequest<>(URL, MovieApiResponse.class, new Response.Listener<MovieApiResponse>() {
            @Override
            public void onResponse(MovieApiResponse response) {
                assert moviesGrid != null;
                movieApiResponse = response;
                initAdapter(moviesGrid, movieApiResponse);
                materialProgressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                errorSection.setVisibility(View.VISIBLE);
                moviesGrid.setVisibility(View.GONE);
                materialProgressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(gsonRequest);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("MovieApiResponse", movieApiResponse);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, @NonNull Cursor data) {
        if (sortByOption == FAVORITE_SORT_ORDER) {
            if (data.getCount() > 0) {
                initAdapter(moviesGrid, getMoviesFromCursor(data));
            } else if (mTwoPane) {
                Toast.makeText(MovieListActivity.this, "Nothing saved yet", Toast.LENGTH_SHORT).show();
            } else {
                showNoFavorite(true);
            }
        }
    }


    @NonNull
    private MovieApiResponse getMoviesFromCursor(@Nullable Cursor cursor) {

        MovieApiResponse movieApiResponse = new MovieApiResponse();
        List<Result> movies = new ArrayList<>();

        if (cursor != null) {
            /*Log.e("cursor length","->"+cursor.getCount());
            Log.e("column length","->"+cursor.getColumnCount());*/

            if (cursor.moveToFirst()) {
                do {

                    int movie_id = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_ID));
                    boolean movie_adult = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_ADULT)) == 1;
                    String movie_poster_path = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH));
                    String movie_overview = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_OVERVIEW));
                    String movie_release_date = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE));
                    String genre = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_GENRES));

                    List<Integer> movie_genre = new ArrayList<>();
                    for (String s : genre.split(","))
                        movie_genre.add(Integer.parseInt(s));

                    String movie_title = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_TITLE));
                    String movie_original_title = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_ORIGINAL_TITLE));
                    String movie_language = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_LANGUAGE));
                    String movie_backdrop_path = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_BACKDROP_PATH));
                    String movie_popularity = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_POPULARITY));
                    boolean movie_video = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_VIDEO)) == 1;
                    String movie_vote_average = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_VOTE_AVERAGE));
                    int movie_vote_count = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_VOTE_COUNT));

                    Result movie = new Result(movie_id, movie_adult, movie_poster_path, movie_overview, movie_release_date, movie_genre
                            , movie_original_title, movie_language, movie_title, movie_backdrop_path, Double.valueOf(movie_popularity), movie_video, Double.valueOf(movie_vote_average), movie_vote_count);

                    movies.add(movie);

                } while (cursor.moveToNext());
            }

        }

        movieApiResponse.setResults(movies);

        return movieApiResponse;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, (MovieListActivity) mContext);

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
