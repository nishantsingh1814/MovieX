package com.eventx.moviex.TvActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.LoginActivity;
import com.eventx.moviex.MovieAdapter.CastAdapter;
import com.eventx.moviex.MovieModels.Rate_value;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.GuestStarAdapter;
import com.eventx.moviex.TvModels.Episodes;
import com.eventx.moviex.TvModels.GuestStars;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SingleEpisodeActivity extends AppCompatActivity {

    ImageView episodePoster;
    Toolbar toolbar;
    TextView episodeName;
    TextView episodeDate;
    TextView episodeOverview;
    RecyclerView guestStarList;
    ArrayList<GuestStars> guestStars;

    GuestStarAdapter adapter;

    TextView ratings;

    TextView rateThis;
    SharedPreferences sp;
    String sessionId;

    SQLiteDatabase db;
    MovieDbHelper helper;
    SQLiteDatabase readDb;
    private boolean alreadyRated;
    private float ratingDb;
    private Episodes episode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_episode);
        Slidr.attach(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        helper=new MovieDbHelper(SingleEpisodeActivity.this);
        db=helper.getWritableDatabase();
        readDb=helper.getReadableDatabase();

        sp = getSharedPreferences("MovieX", Context.MODE_PRIVATE);
        sessionId = sp.getString("session", null);

        episode = (Episodes) getIntent().getSerializableExtra("Episode");
        episodePoster = (ImageView) findViewById(R.id.episode_poster);
        episodeName = (TextView) findViewById(R.id.episode_name);
        episodeOverview = (TextView) findViewById(R.id.episode_overview);
        episodeDate = (TextView) findViewById(R.id.episode_date);
        ratings = (TextView) findViewById(R.id.episode_ratings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        guestStars = new ArrayList<>();
        guestStarList = (RecyclerView) findViewById(R.id.episode_guest_star);
        toolbar.setTitle(getIntent().getStringExtra("tvShow"));
        toolbar.setSubtitle("S" + episode.getSeason_number() + "E" + episode.getEpisode_number());
        setSupportActionBar(toolbar);

        ratings.setText(episode.getVote_average() + "(" + episode.getVote_count() + ")");
        guestStars.addAll(episode.getGuest_stars());

        adapter = new GuestStarAdapter(guestStars, this);
        guestStarList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + episode.getStill_path()).into(episodePoster);
        episodeName.setText(episode.getName());
        episodeOverview.setText(episode.getOverview());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (episode.getAir_date() != null) {
            try {
                date = sdfd.parse(episode.getAir_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            episodeDate.setText(sdf.format(date));
        }

        rateThis = (TextView)findViewById(R.id.rate_this);
        setRating();

        rateThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionId != null) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SingleEpisodeActivity.this);
                    View v = LayoutInflater.from(SingleEpisodeActivity.this).inflate(R.layout.dialog_layout, null);
                    alertDialog.setView(v);
                    ImageView image = (ImageView) v.findViewById(R.id.image);
                    TextView title = (TextView) v.findViewById(R.id.title);
                    Button save = (Button) v.findViewById(R.id.save_review);
                    final Button remove = (Button) v.findViewById(R.id.remove_review);
                    final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
                    ratingBar.setRating(ratingDb/2);
                    final AlertDialog dialog = alertDialog.create();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Rate_value rate_value = new Rate_value();
                            if(ratingBar.getRating()==0.0){
                                Toast.makeText(SingleEpisodeActivity.this,"0 Rating not allowed",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            rate_value.setValue(2 * ratingBar.getRating());
                            ApiInterface apiInterface = ApiClient.getApiInterface();
                            Call<Rate_value> raviewCall = apiInterface.rateTvShowEpisode(rate_value,episode.getId(),episode.getSeason_number(),episode.getEpisode_number(), sessionId);
                            raviewCall.enqueue(new Callback<Rate_value>() {
                                @Override
                                public void onResponse(Call<Rate_value> call, Response<Rate_value> response) {
                                    Toast.makeText(SingleEpisodeActivity.this, "Saving Rating", Toast.LENGTH_SHORT).show();
                                    if(alreadyRated){
                                        helper.updateRating(db,MovieDbHelper.TV_EPISODE_RATED_TABLE,episode.getId(),rate_value.getValue());
                                    }else {
                                        helper.addRating(db, MovieDbHelper.TV_EPISODE_RATED_TABLE, episode.getId(), rate_value.getValue());
                                    }
                                    setRating();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Rate_value> call, Throwable t) {

                                }
                            });
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ApiInterface apiInterface = ApiClient.getApiInterface();
                            Call<Rate_value> removeCall = apiInterface.deleteRatingTvEpidode(episode.getId(),episode.getSeason_number(),episode.getEpisode_number(), sessionId);
                            removeCall.enqueue(new Callback<Rate_value>() {
                                @Override
                                public void onResponse(Call<Rate_value> call, Response<Rate_value> response) {
                                    Toast.makeText(SingleEpisodeActivity.this, "Deleting Rating", Toast.LENGTH_SHORT).show();
                                    db.delete(MovieDbHelper.TV_EPISODE_RATED_TABLE, MovieDbHelper.COLUMN_TV_EPISODE_RATED_ID + " = " + episode.getId(), null);
                                    setRating();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Rate_value> call, Throwable t) {

                                }
                            });
                        }
                    });
                    Picasso.with(SingleEpisodeActivity.this).load("https://image.tmdb.org/t/p/w500" + episode.getStill_path()).into(image);
                    title.setText(episode.getName());
                    dialog.show();
                } else {
                    startActivity(new Intent(SingleEpisodeActivity.this, LoginActivity.class));
                }


            }
        });


    }
    private void setRating() {


        Cursor c = readDb.query(MovieDbHelper.TV_EPISODE_RATED_TABLE, null, MovieDbHelper.COLUMN_TV_EPISODE_RATED_ID + " = " +episode.getId() , null, null, null, null);
        if(c.getCount()!=0){
            c.moveToFirst();
            alreadyRated=true;
            ratingDb=c.getLong(c.getColumnIndex(MovieDbHelper.COLUMN_RATING));
            rateThis.setText("Your Rating :"+ratingDb);
        }
        else{
            alreadyRated=false;

            rateThis.setText("Rate this");
            ratingDb=0.0f;
        }
        c.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_left);

    }
}
