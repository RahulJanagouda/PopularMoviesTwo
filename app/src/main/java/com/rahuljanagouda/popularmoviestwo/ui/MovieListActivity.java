package com.rahuljanagouda.popularmoviestwo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rahuljanagouda.popularmoviestwo.AppController;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.adapters.GridSpacingItemDecoration;
import com.rahuljanagouda.popularmoviestwo.adapters.RecyclerAdapter;
import com.rahuljanagouda.popularmoviestwo.customViews.MaterialProgressDialog;
import com.rahuljanagouda.popularmoviestwo.helper.GsonRequest;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.MovieApiResponse;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Context mContext;
    private RecyclerView moviesGrid;
    private MovieApiResponse movieApiResponse = null;
    private LinearLayout errorSection;

    private MaterialProgressDialog materialProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        moviesGrid = (RecyclerView) findViewById(R.id.movie_list);

        errorSection = (LinearLayout) findViewById(R.id.errorSection);


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        materialProgressDialog = MaterialProgressDialog.show(mContext,"Listing movies...", false,true);
        if (savedInstanceState == null || !savedInstanceState.containsKey("MovieApiResponse")) {
            checkInternetAndRequestData();
        } else {
            movieApiResponse = savedInstanceState.getParcelable("MovieApiResponse");
            setupRecyclerView(moviesGrid, movieApiResponse);
        }

//        materialProgressDialog.;

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, MovieApiResponse response) {

        recyclerView.setAdapter(new RecyclerAdapter(mContext, response, mTwoPane));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,10,true));
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
        materialProgressDialog.dismiss();

    }


    private void checkInternetAndRequestData(){
        if (Network.isOnline(mContext)){
            makeNetworkRequest(1);
        }else {
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
                    checkInternetAndRequestData();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void makeNetworkRequest(int i) {

        String URL = Network.URL_TMDB_DISCOVER_MOVIES_POPULAR_API;
        if (i == 1) {
            URL = Network.URL_TMDB_DISCOVER_MOVIES_HIGHEST_RATED_API;
        }

        GsonRequest<MovieApiResponse> gsonRequest = new GsonRequest<>(URL, MovieApiResponse.class, new Response.Listener<MovieApiResponse>() {
            @Override
            public void onResponse(MovieApiResponse response) {
                assert moviesGrid != null;
                movieApiResponse = response;
                setupRecyclerView(moviesGrid, movieApiResponse);

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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("MovieApiResponse", movieApiResponse);
        super.onSaveInstanceState(outState);
    }
}
