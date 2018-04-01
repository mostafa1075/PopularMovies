package com.mostafa1075.popularmovies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.mostafa1075.popularmovies.pojo.MovieReview;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView reviewerNameTv = findViewById(R.id.reviewer_name);
        TextView reviewTv = findViewById(R.id.review);

        Intent intent = getIntent();
        if (intent == null)
            finish();
        MovieReview review = intent.getParcelableExtra(DetailActivity.REVIEW_DATA_KEY);

        reviewerNameTv.setText(review.getAuthor());
        reviewTv.setText(review.getContent());
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
