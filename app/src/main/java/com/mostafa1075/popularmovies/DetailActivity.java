package com.mostafa1075.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mostafa1075.popularmovies.databinding.ActivityDetailBinding;
import com.mostafa1075.popularmovies.model.MovieDetails;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    // TODO: Create a better layout for DetailActivity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActivityDetailBinding detailBinding;
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if(intent == null)
            finish();
        MovieDetails movieDetails = intent.getParcelableExtra(MainActivity.MOVIE_DATA_KEY);

        setTitle(movieDetails.getTitle());
        Picasso.with(this)
                .load(movieDetails.getPosterUrl())
                .into(detailBinding.ivPoster);
        detailBinding.tvOverview.setText(movieDetails.getOverview());
        detailBinding.tvRating.setText("" + movieDetails.getRating() + "/10");
        detailBinding.tvYear.setText(movieDetails.getReleaseDate());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
