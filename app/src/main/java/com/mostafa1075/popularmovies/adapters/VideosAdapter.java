package com.mostafa1075.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mostafa1075.popularmovies.R;
import com.mostafa1075.popularmovies.pojo.MovieVideo;
import com.mostafa1075.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mosta on 10-Mar-18.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoAdapterViewHolder> {

    private final Context mContext;
    private final VideosAdapterOnClickHandler mClickHandler;
    private ArrayList<MovieVideo> mVideos;

    public VideosAdapter(Context context, VideosAdapterOnClickHandler clickHandler) {
        mVideos = new ArrayList<>();
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.video_single_item, parent, false);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {

        String videoName = mVideos.get(position).getName();
        String videoPath = mVideos.get(position).getKey();
        String videoThumbnailUrl = NetworkUtils.buildVideoThumbnailUrl(videoPath);

        holder.mVideoNameTextView.setText(videoName);
        Picasso.with(mContext)
                .load(videoThumbnailUrl)
                .into(holder.mVideoThumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void swipeData(ArrayList<MovieVideo> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    public interface VideosAdapterOnClickHandler {
        void onVideoClick(MovieVideo video);
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final ImageView mVideoThumbnailImageView;
        private final TextView mVideoNameTextView;

        VideoAdapterViewHolder(View itemView) {
            super(itemView);
            mVideoNameTextView = itemView.findViewById(R.id.tv_video_name);
            mVideoThumbnailImageView = itemView.findViewById(R.id.iv_video_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onVideoClick(mVideos.get(position));
        }
    }

}
