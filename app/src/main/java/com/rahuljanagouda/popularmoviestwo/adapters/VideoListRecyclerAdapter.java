package com.rahuljanagouda.popularmoviestwo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.pojo.videos.Result;

import java.util.List;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class VideoListRecyclerAdapter extends RecyclerView.Adapter<VideoListRecyclerAdapter.ViewHolder> {


    private final Context mContext;
    private final List<Result> videos;
    private Callback mCallback;

    public VideoListRecyclerAdapter(Context mContext, List<Result> videos) {
        this.mContext = mContext;
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_video, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Result video = videos.get(position);

        Glide
                .with(mContext)
                .load("http://img.youtube.com/vi/" + video.getKey() + "/0.jpg")
                .into(holder.videoThumb);

        holder.videoName.setText(video.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onVideoClick(video);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (videos != null ? videos.size() : 0);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onVideoClick(Result video);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView videoThumb;
        final TextView videoName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumb = (ImageView) itemView.findViewById(R.id.videoThumb);
            videoName = (TextView) itemView.findViewById(R.id.videoName);
        }
    }
}
