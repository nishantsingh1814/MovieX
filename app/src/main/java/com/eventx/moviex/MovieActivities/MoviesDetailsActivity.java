package com.eventx.moviex.MovieActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eventx.moviex.MainActivity;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.PeopleModels.PopularPeople;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton mPlayTrailer;
    ImageView mBackdropImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPlayTrailer = (ImageButton) findViewById(R.id.movie_trailer);
        mBackdropImage = (ImageView) findViewById(R.id.backdrop_image);
        mPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<ResultTrailer> keyResult = apiInterface.getTrailerKey(getIntent().getLongExtra("id", -1));
                keyResult.enqueue(new Callback<ResultTrailer>() {
                    @Override
                    public void onResponse(Call<ResultTrailer> call, Response<ResultTrailer> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResults().size() == 0) {
                                Snackbar.make(v, "No trailer Available", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            String key = response.body().getResults().get(0).getKey();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultTrailer> call, Throwable t) {
                    }
                });
            }
        });
        fetchData();

    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<MovieDetails> movieDetails = apiInterface.getMovieDetails(getIntent().getLongExtra("id", -1));
        movieDetails.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful()) {
                    Picasso.with(MoviesDetailsActivity.this).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_Home) {
            startActivity(new Intent(MoviesDetailsActivity.this, MainActivity.class));
        }
        if (item.getItemId() == R.id.nav_movie) {
            startActivity(new Intent(MoviesDetailsActivity.this, MoviesActivity.class));
        }
        if (item.getItemId() == R.id.nav_tv) {
            startActivity(new Intent(MoviesDetailsActivity.this, TvActivity.class));
        }
        if(item.getItemId()==R.id.nav_people){
            startActivity(new Intent(MoviesDetailsActivity.this, PopularPeopleActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
