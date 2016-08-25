package com.rahuljanagouda.popularmoviestwo.ui.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.database.DatabaseChangedReceiver;
import com.rahuljanagouda.popularmoviestwo.database.MoviesContract;
import com.rahuljanagouda.popularmoviestwo.database.MoviesOpenHelper;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.utils.Constatns;
import com.rahuljanagouda.popularmoviestwo.utils.General;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class OverviewFragment extends Fragment {

    private Animation pulseAnimation;
    private ImageView movieThumb;
    private ImageView favoriteImage;
    private Context mContext;
    private TextView movieOverview, movieTitle, movieRleaseDate, movieRating;
    @Nullable
    private Result movie;
    private boolean isFavorite;

    @NonNull
    public static OverviewFragment newInstance(@Nullable Result movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cant be null");
        }

        Bundle args = new Bundle();
        args.putParcelable(Constatns.MOVIE_PARCEL_KEY, movie);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = this.getActivity();

        pulseAnimation = AnimationUtils.loadAnimation(mContext, R.anim.pulse);


        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movie = getArguments().getParcelable(Constatns.MOVIE_PARCEL_KEY);

        isFavorite = General.isMovieFavorite(mContext, String.valueOf(movie.getId()));

        movieOverview = (TextView) view.findViewById(R.id.movieOverview);
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        movieRleaseDate = (TextView) view.findViewById(R.id.movieRleaseDate);
        movieRating = (TextView) view.findViewById(R.id.movieRating);
        movieThumb = (ImageView) view.findViewById(R.id.movieThumb);
        favoriteImage = (ImageView) view.findViewById(R.id.favoriteImage);

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

        if (isFavorite) {
            favoriteImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            favoriteImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) {
                    favoriteImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    favoriteImage.startAnimation(pulseAnimation);
                    mContext.getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build(), null, null);
                    mContext.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
                    isFavorite = false;
                    Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();

                } else {
                    favoriteImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favoriteImage.startAnimation(pulseAnimation);
                    ContentValues values = MoviesOpenHelper.getMovieContentValues(movie);
                    mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, values);
                    mContext.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
                    isFavorite = true;
                    Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();

                }
            }
        });


        movieOverview.setText(movie.getOverview());
        movieRleaseDate.setText(movie.getReleaseDate());
        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        movieTitle.setText(movie.getTitle());

    }

}
