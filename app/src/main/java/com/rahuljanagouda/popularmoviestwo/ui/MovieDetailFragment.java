package com.rahuljanagouda.popularmoviestwo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.rahuljanagouda.popularmoviestwo.AppController;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.helper.GsonRequest;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.pojo.videos.VideoResponse;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

import java.util.List;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    public static final String MOVIE_RESULT_KEY = "movieResultKey";

    private Result movieResult;

    public List<com.rahuljanagouda.popularmoviestwo.pojo.videos.Result> videoResult;

    public AppCompatActivity mContext;

    public Toolbar toolbar;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(MOVIE_RESULT_KEY)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            movieResult = getArguments().getParcelable(MOVIE_RESULT_KEY);


            if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_RESULT_KEY)) {
                movieResult = getArguments().getParcelable(MOVIE_RESULT_KEY);
            } else {
                movieResult = savedInstanceState.getParcelable(MOVIE_RESULT_KEY);
            }

//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
//            }


            mContext = (AppCompatActivity) this.getActivity();





        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_details, container, false);

        // Show the dummy content as text in a TextView.
        if (movieResult != null) {
//            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(mItem.details);

            String URL = Network.TMDB_VIDEO_URL.replace("{id}",String.valueOf(movieResult.getId()));

            GsonRequest<VideoResponse> gsonRequest = new GsonRequest<>(URL, VideoResponse.class, new Response.Listener<VideoResponse>() {
                @Override
                public void onResponse(VideoResponse response) {
                    videoResult = response.getResults();

                    TextView movieVideos = (TextView) rootView.findViewById(R.id.movieVideos);
                    movieVideos.setText(videoResult.get(0).getName());
                    movieVideos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Network.watchYoutubeVideo(mContext,videoResult.get(0).getKey());
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    videoResult = null;

                    TextView movieVideos = (TextView) rootView.findViewById(R.id.movieVideos);
                    movieVideos.setVisibility(View.GONE);
                }
            });
            AppController.getInstance().addToRequestQueue(gsonRequest);


//            if (mContext instanceof MovieDetailActivity){
                ImageView movieThumb = (ImageView) rootView.findViewById(R.id.movieThumb);
                if (movieResult.getPosterPath() != null) {
                    Glide
                            .with(mContext)
                            .load(Network.TMDB_IMAGE_HQ_BASE_URL + movieResult.getPosterPath())
                            .error(R.drawable.placeholder)
                            .into(movieThumb);

                } else {
                    Glide
                            .with(mContext)
                            .load(R.drawable.placeholder)
                            .into(movieThumb);
                }

//                toolbar = (Toolbar) mContext.findViewById(R.id.toolbar);
//
//                toolbar.setTitle(movieResult.getTitle());
//
//                mContext.setSupportActionBar(toolbar);
//
//                mContext.getSupportActionBar().setTitle(movieResult.getTitle());
//
//                mContext.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//            }

            TextView movieOverview = (TextView) rootView.findViewById(R.id.movieOverview);
            TextView movieTitle = (TextView) rootView.findViewById(R.id.movieTitle);
            TextView movieRleaseDate = (TextView) rootView.findViewById(R.id.movieRleaseDate);
            TextView movieRating = (TextView) rootView.findViewById(R.id.movieRating);

            movieOverview.setText(movieResult.getOverview());
            movieRleaseDate.setText(movieResult.getReleaseDate());
            movieRating.setText(String.valueOf(movieResult.getVoteAverage()));
            movieTitle.setText(movieResult.getTitle());

        }

        return rootView;
    }
}
