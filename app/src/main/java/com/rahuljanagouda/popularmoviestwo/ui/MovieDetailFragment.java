package com.rahuljanagouda.popularmoviestwo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.adapters.MovieDetailsTabAdapter;
import com.rahuljanagouda.popularmoviestwo.customViews.CustomTabLayout;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.utils.General;

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
    public List<com.rahuljanagouda.popularmoviestwo.pojo.videos.Result> videoResult;
    public AppCompatActivity mContext;
    public Toolbar toolbar;
    @Nullable
    private Result movieResult;

    private boolean isFavorite;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_details, container, false);

        // Show the dummy content as text in a TextView.
        if (movieResult != null) {
//            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(mItem.details);


//            if (mContext instanceof MovieDetailActivity){


            MovieDetailsTabAdapter movieDetailsTabAdapter = new MovieDetailsTabAdapter(getChildFragmentManager(), movieResult);

            ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
            CustomTabLayout tabLayout = (CustomTabLayout) rootView.findViewById(R.id.tabLayout);

            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(movieDetailsTabAdapter);

            tabLayout.setTabTextColors(getResources().getColor(R.color.bluegrey_300), getResources().getColor(R.color.white));
//            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
            tabLayout.setSelectedTabIndicatorHeight(General.dpToPx(2, getActivity()));
            tabLayout.setupWithViewPager(mViewPager, mContext);

//            movieOverview.setText(movieResult.getOverview());


        }

        return rootView;
    }
}
