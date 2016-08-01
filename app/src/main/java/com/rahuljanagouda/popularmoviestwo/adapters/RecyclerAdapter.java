package com.rahuljanagouda.popularmoviestwo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.MovieApiResponse;
import com.rahuljanagouda.popularmoviestwo.pojo.movie.Result;
import com.rahuljanagouda.popularmoviestwo.ui.MovieDetailActivity;
import com.rahuljanagouda.popularmoviestwo.ui.MovieDetailFragment;
import com.rahuljanagouda.popularmoviestwo.utils.Network;


/**
 * Created by rahuljanagouda on 24/07/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final MovieApiResponse pageResponse;
    private boolean mTwoPane;

    public RecyclerAdapter(Context mContext, MovieApiResponse response, boolean mTwoPane) {
        this.mContext = mContext;
        pageResponse = response;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Result movie = pageResponse.getResults().get(position);

        holder.movieName.setText(movie.getTitle());
        if (movie.getPosterPath() != null) {
            Glide
                    .with(mContext)
                    .load(Network.TMDB_IMAGE_BASE_URL + movie.getPosterPath())
                    .error(R.drawable.placeholder)
                    .into(holder.card_image);
        } else {
            Glide
                    .with(mContext)
                    .load(R.drawable.placeholder)
                    .into(holder.card_image);
        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(mContext, DetailsActivity.class);
//                i.putExtra("movieResult", movie);
//                mContext.startActivity(i);
//
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.MOVIE_RESULT_KEY, movie);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();

                    Intent i = new Intent(mContext, MovieDetailActivity.class);
                    i.putExtra("movieResult", movie);

                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.MOVIE_RESULT_KEY, movie);

                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pageResponse.getResults().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView card_image;
        TextView movieName;

        public ViewHolder(View itemView) {
            super(itemView);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            movieName = (TextView) itemView.findViewById(R.id.movieName);
        }
    }
}
