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
import java.util.List;

/**
 * Created by mosta on 10-Mar-18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<MovieReview> mReviews;
    private Context mContext;

    public ReviewAdapter(Context context) {
        mReviews = new ArrayList<MovieReview>();
        mContext = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
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

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mReviewerNameTextView;
        private TextView mReviewTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewerNameTextView = (TextView) itemView.findViewById(R.id.tv_reviewer_name);
            mReviewTextView = (TextView) itemView.findViewById(R.id.tv_review);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
