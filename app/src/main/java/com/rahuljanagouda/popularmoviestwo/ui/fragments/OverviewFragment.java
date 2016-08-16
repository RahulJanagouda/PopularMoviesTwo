package com.rahuljanagouda.popularmoviestwo.ui.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.utils.Constatns;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class OverviewFragment extends Fragment {

    private TextView movieOverview,movieTitle,movieRleaseDate,movieRating;
    private Result movie;
    ImageView movieThumb;
    Context mContext;

    public static OverviewFragment newInstance(Result movie){
        if (movie == null){
            throw new IllegalArgumentException("Movie cant be null");
        }

        Bundle args = new Bundle();
        args.putParcelable(Constatns.MOVIE_PARCEL_KEY,movie);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = this.getActivity();
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movie = getArguments().getParcelable(Constatns.MOVIE_PARCEL_KEY);
        movieOverview = (TextView) view.findViewById(R.id.movieOverview);
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        movieRleaseDate = (TextView) view.findViewById(R.id.movieRleaseDate);
        movieRating = (TextView) view.findViewById(R.id.movieRating);
        movieThumb = (ImageView) view.findViewById(R.id.movieThumb);

        inflateData();
    }


    private void inflateData() {

        if (movie.getPosterPath() != null) {
            Glide
                    .with(mContext)
                    .load(Network.TMDB_IMAGE_HQ_BASE_URL + movie.getPosterPath())
                    .error(R.drawable.placeholder)
                    .into(movieThumb);

        } else {
            Glide
                    .with(mContext)
                    .load(R.drawable.placeholder)
                    .into(movieThumb);
        }

        movieOverview.setText(movie.getOverview());

        movieRleaseDate.setText(movie.getReleaseDate());
        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        movieTitle.setText(movie.getTitle());

    }

}
