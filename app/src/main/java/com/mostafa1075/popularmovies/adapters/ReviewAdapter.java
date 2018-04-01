package com.mostafa1075.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mostafa1075.popularmovies.R;
import com.mostafa1075.popularmovies.pojo.MovieReview;

import java.util.ArrayList;

/**
 * Created by mosta on 10-Mar-18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final Context mContext;
    private final ReviewAdapterOnClickHandler mClickHandler;
    private ArrayList<MovieReview> mReviews;

    public ReviewAdapter(Context context, ReviewAdapterOnClickHandler handler) {
        mReviews = new ArrayList<>();
        mContext = context;
        mClickHandler = handler;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.review_single_item, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        String author = mReviews.get(position).getAuthor();
        String review = mReviews.get(position).getContent();
        holder.mReviewerNameTextView.setText(author);
        holder.mReviewTextView.setText(review);
    }

    @Override
    public int getItemCount() {return mReviews.size();}

    public void swipeData(ArrayList<MovieReview> reviews){
        mReviews = reviews;
        notifyDataSetChanged();

    }
    public interface ReviewAdapterOnClickHandler {
        void onReviewClick(MovieReview review);
    }
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mReviewerNameTextView;
        private final TextView mReviewTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewerNameTextView = itemView.findViewById(R.id.tv_reviewer_name);
            mReviewTextView = itemView.findViewById(R.id.tv_review);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onReviewClick(mReviews.get(position));
        }
    }
}
