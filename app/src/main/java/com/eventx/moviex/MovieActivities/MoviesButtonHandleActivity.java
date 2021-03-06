package com.eventx.moviex.MovieActivities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.R;

public class MoviesButtonHandleActivity extends AppCompatActivity implements MoviesButtonHandleFragment.MovieClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_button_handle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getStringExtra("button").equals("Most Popular")) {
            getSupportActionBar().setTitle("Most Popular Movies");
        }
        if (getIntent().getStringExtra("button").equals("Top Rated")) {
            getSupportActionBar().setTitle("Top 100 Rated Movies");
        }
        if (getIntent().getStringExtra("button").equals("Now Showing")) {
            getSupportActionBar().setTitle("Movies In Theatres");
        }
        if (getIntent().getStringExtra("button").equals("Upcoming")) {
            getSupportActionBar().setTitle("Upcoming Movies");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MoviesButtonHandleFragment movieFragment=(MoviesButtonHandleFragment) getSupportFragmentManager().findFragmentById(R.id.movie_btn_handle_frag);
        movieFragment.setListener(this);
    }

    @Override
    public void movieClickListener(Movie movieClass) {
        Intent moviesDetailIntent = new Intent(MoviesButtonHandleActivity.this, MoviesDetailsActivity.class);
        moviesDetailIntent.putExtra("id",movieClass.getMovieId());
        moviesDetailIntent.putExtra("title",movieClass.getTitle());
        moviesDetailIntent.putExtra("poster",movieClass.getPoster_path());
        startActivity(moviesDetailIntent);
        overridePendingTransition(R.anim.slide_in,R.anim.no_change);
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
}
