package com.eventx.moviex.TvActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eventx.moviex.MovieActivities.MoviesButtonHandleActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.R;
import com.eventx.moviex.TvFragments.TvbuttonHandleFragment;
import com.eventx.moviex.TvModels.TvShow;

public class TvButtonHandleActivity extends AppCompatActivity implements TvbuttonHandleFragment.VerticalTvClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_button_handle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getStringExtra("button").equals("Most Popular")) {
            getSupportActionBar().setTitle("Most Popular Tv Shows");
        }
        if (getIntent().getStringExtra("button").equals("Top Rated")) {
            getSupportActionBar().setTitle("Top 100 Rated Tv Shows");
        }
        if (getIntent().getStringExtra("button").equals("Tv Tonight")) {
            getSupportActionBar().setTitle("Airing Tonight");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TvbuttonHandleFragment tvbuttonHandleFragment=(TvbuttonHandleFragment)getSupportFragmentManager().findFragmentById(R.id.tv_button_handle_frag);
        tvbuttonHandleFragment.setVerticalTvClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change,R.anim.slide_left);

    }

    @Override
    public void verticalTvClick(TvShow show) {
        Intent moviesDetailIntent = new Intent(TvButtonHandleActivity.this, TvShowDetailsActivity.class);
        moviesDetailIntent.putExtra("id",show.getTvId());
        moviesDetailIntent.putExtra("title",show.getName());
        moviesDetailIntent.putExtra("poster",show.getPoster_path());
        startActivity(moviesDetailIntent);
        overridePendingTransition(R.anim.slide_in,R.anim.no_change);
    }
}
