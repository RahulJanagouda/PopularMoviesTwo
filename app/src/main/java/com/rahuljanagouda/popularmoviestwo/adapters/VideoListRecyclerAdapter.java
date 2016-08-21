package com.rahuljanagouda.popularmoviestwo.adapters;

import android.content.Context;
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


    public interface Callback {
        public void onVideoClick(Result video);
    }


    Callback mCallback;
    Context mContext;
    List<Result> videos;

    public VideoListRecyclerAdapter(Context mContext, List<Result> videos) {
        this.mContext = mContext;
        this.videos = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_video, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result video = videos.get(position);

        Glide
                .with(mContext)
                .load("http://img.youtube.com/vi/" + video.getKey() + "/0.jpg")
                .into(holder.videoThumb);

        holder.videoName.setText(video.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback!=null) {
                    mCallback.onVideoClick(video);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (videos != null ? videos.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoThumb;
        TextView videoName;

        public ViewHolder(View itemView) {
            super(itemView);
            videoThumb = (ImageView) itemView.findViewById(R.id.videoThumb);
            videoName = (TextView) itemView.findViewById(R.id.videoName);
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
