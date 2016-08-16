package com.rahuljanagouda.popularmoviestwo.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahuljanagouda.popularmoviestwo.R;
import com.rahuljanagouda.popularmoviestwo.pojo.reviews.Result;
import com.rahuljanagouda.popularmoviestwo.utils.General;

import java.util.List;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class ReviewsListRecyclerAdapter extends RecyclerView.Adapter<ReviewsListRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Result> reviews;

    public ReviewsListRecyclerAdapter(Context mContext, List<Result> reviews) {
        this.mContext = mContext;
        this.reviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_review, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result review = reviews.get(position);

        holder.mReviewUser.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return (reviews != null ? reviews.size() : 0);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mReviewUser, mReviewContent;
        private ImageView mAvatar;


        public ViewHolder(View itemView) {
            super(itemView);

            mReviewUser = (TextView) itemView.findViewById(R.id.reviewUser);
            mReviewContent = (TextView) itemView.findViewById(R.id.reviewContent);
            mAvatar = (ImageView) itemView.findViewById(R.id.reviewAvatar);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setElevation(mAvatar, General.dpToPx(5, mContext));
                ViewCompat.setTranslationZ(mAvatar, General.dpToPx(5, mContext));

            } else {
                mAvatar.bringToFront(); // works on both pre-lollipop and Lollipop
            }
        }
    }
}
