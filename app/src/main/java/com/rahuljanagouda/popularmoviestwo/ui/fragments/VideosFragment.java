package com.rahuljanagouda.popularmoviestwo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.rahuljanagouda.popularmoviestwo.AppController;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.adapters.GridSpacingItemDecoration;
import com.rahuljanagouda.popularmoviestwo.adapters.VideoListRecyclerAdapter;
import com.rahuljanagouda.popularmoviestwo.helper.GsonRequest;
import com.rahuljanagouda.popularmoviestwo.pojo.videos.Result;
import com.rahuljanagouda.popularmoviestwo.pojo.videos.VideoResponse;
import com.rahuljanagouda.popularmoviestwo.utils.Constatns;
import com.rahuljanagouda.popularmoviestwo.utils.Network;

import java.util.List;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class VideosFragment extends Fragment implements VideoListRecyclerAdapter.Callback {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private RecyclerView videosRecycler;
    private LinearLayoutManager linearLayoutManager;
    private VideoListRecyclerAdapter mVideoListRecyclerAdapter;

    List<Result> videos;
    com.rahuljanagouda.popularmoviestwo.pojo.movie.Result movie;
    Context mContext;
    private View noVideosView;



    public static VideosFragment newInstance(com.rahuljanagouda.popularmoviestwo.pojo.movie.Result movie){
        if (movie == null) {
            throw new IllegalArgumentException("Movies cant be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(Constatns.MOVIE_PARCEL_KEY, movie);

        VideosFragment fragment = new VideosFragment();
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
            getVideosData();
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

    public void getVideosData() {

        String URL = Network.TMDB_VIDEO_URL.replace("{id}",String.valueOf(movie.getId()));

        GsonRequest<VideoResponse> gsonRequest = new GsonRequest<>(URL, VideoResponse.class, new Response.Listener<VideoResponse>() {
            @Override
            public void onResponse(VideoResponse response) {


                if(response.getResults().size()>0) {
                    initAdapter(response.getResults());
                } else {
                    showNoReviews(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                videos = null;
            }
        });
        AppController.getInstance().addToRequestQueue(gsonRequest);
    }


    private void initAdapter(List<Result> videos) {
        showNoReviews(false);
        mVideoListRecyclerAdapter = new VideoListRecyclerAdapter(mContext,videos);
        mVideoListRecyclerAdapter.setCallback(this);
        videosRecycler.setAdapter(mVideoListRecyclerAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videosRecycler = (RecyclerView) view.findViewById(R.id.videosRecycler);
        noVideosView = view.findViewById(R.id.errorMessage);


//        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        videosRecycler.setLayoutManager(linearLayoutManager);

        videosRecycler.setHasFixedSize(true);
        videosRecycler.addItemDecoration(new GridSpacingItemDecoration(2,3,true));
        videosRecycler.setLayoutManager(new GridLayoutManager(mContext,2));



//        int spacing = General.dpToPx(5, getActivity());
//        videosRecycler.addItemDecoration(new VerticalSpaceItemDecoration(spacing));

        initAdapter(videos);
    }

    @Override
    public void onVideoClick(Result video) {
        inflateVideoPlayer(video.getKey());

    }

    private void inflateVideoPlayer(String videoKey) {

        int startTimeMillis = 0;
        boolean autoplay = true;
        boolean lightboxMode = false;

        Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                getActivity(), Constatns.YOUTUBE_API_KEY, videoKey, startTimeMillis, autoplay, lightboxMode);

        if (intent != null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
            } else {
                // Could not resolve the intent - must need to install or update the YouTube API service.
                YouTubeInitializationResult.SERVICE_MISSING
                        .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != getActivity().RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), 0).show();
            } else {
                String errorMessage = getResources().getString(R.string.player_error) + errorReason.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showNoReviews(boolean value){

        int noReviewsVisibility = value? View.VISIBLE : View.GONE;
        noVideosView.setVisibility(noReviewsVisibility);

        int recyclerViewVisibility = value? View.GONE : View.VISIBLE;
        videosRecycler.setVisibility(recyclerViewVisibility);
    }

}
