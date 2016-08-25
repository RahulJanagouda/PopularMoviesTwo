package com.rahuljanagouda.popularmoviestwo.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.ui.fragments.OverviewFragment;
import com.rahuljanagouda.popularmoviestwo.ui.fragments.ReviewsFragment;
import com.rahuljanagouda.popularmoviestwo.ui.fragments.VideosFragment;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class MovieDetailsTabAdapter extends FragmentStatePagerAdapter {

    private final int PAGE_COUNT = 3;
    @NonNull
    private final String[] tabTitles = new String[]{"Overview", "Videos", "Reviews"};
    private final Result movie;
//    private Movies movies;
private FragmentManager fm;


    public MovieDetailsTabAdapter(FragmentManager fm, Result movie) {
        super(fm);
        this.movie = movie;
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(movie);
            case 1:
                return VideosFragment.newInstance(movie);
            case 2:
                return ReviewsFragment.newInstance(movie);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
