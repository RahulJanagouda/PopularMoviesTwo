package com.rahuljanagouda.popularmoviestwo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rahuljanagouda.popularmoviestwo.AppController;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.adapters.ReviewsListRecyclerAdapter;
import com.rahuljanagouda.popularmoviestwo.helper.GsonRequest;
import com.rahuljanagouda.popularmoviestwo.pojo.reviews.Result;
import com.rahuljanagouda.popularmoviestwo.pojo.reviews.ReviewsResponse;
import com.rahuljanagouda.popularmoviestwo.utils.Constatns;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class ReviewsFragment extends Fragment {
    private Context mContext;
    private RecyclerView reviewRecycler;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsListRecyclerAdapter reviewsAdapter;
    private List<Result> reviews = new ArrayList<>();
    private com.rahuljanagouda.popularmoviestwo.pojo.movie.Result movie;
    private View noReviewsView;

    public static ReviewsFragment newInstance(com.rahuljanagouda.popularmoviestwo.pojo.movie.Result movie){
        if (movie == null) {
            throw new IllegalArgumentException("The Movies Data can not be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(Constatns.MOVIE_PARCEL_KEY, movie);

        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = this.getActivity();
        movie = getArguments().getParcelable(Constatns.MOVIE_PARCEL_KEY);
        checkInternetAndRequestData();
    }


    private void checkInternetAndRequestData(){
        if (Network.isOnline(mContext)){
            getReviewsData();
        }else {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(mContext);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Oops, No internet connection found. Please connect and retry again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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

    private void getReviewsData() {
        String URL = Network.TMDB_REVIEW_URL.replace("{id}",String.valueOf(movie.getId()));

        GsonRequest<ReviewsResponse> gsonRequest = new GsonRequest<>(URL, ReviewsResponse.class, new Response.Listener<ReviewsResponse>() {
            @Override
            public void onResponse(ReviewsResponse response) {
//                reviews = response.getResults();

                if(response.getTotalResults()>0) {
                    initAdapter(response.getResults());
                } else {
                    showNoReviews(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                reviews = null;
            }
        });
        AppController.getInstance().addToRequestQueue(gsonRequest);
    }

    private void initAdapter(List<Result> reviews) {
        showNoReviews(false);
        reviewsAdapter = new ReviewsListRecyclerAdapter(mContext,reviews);
        reviewRecycler.setAdapter(reviewsAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reviewRecycler = (RecyclerView) view.findViewById(R.id.reviewsRecycler);
        noReviewsView = view.findViewById(R.id.errorMessage);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        reviewRecycler.setLayoutManager(linearLayoutManager);
//        int spacing = General.dpToPx(5, getActivity());
//        reviewRecycler.addItemDecoration(new VerticalSpaceItemDecoration(spacing));

        initAdapter(reviews);
    }

    private void showNoReviews(boolean value){

        int noReviewsVisibility = value? View.VISIBLE : View.GONE;
        noReviewsView.setVisibility(noReviewsVisibility);

        int recyclerViewVisibility = value? View.GONE : View.VISIBLE;
        reviewRecycler.setVisibility(recyclerViewVisibility);
    }
}
