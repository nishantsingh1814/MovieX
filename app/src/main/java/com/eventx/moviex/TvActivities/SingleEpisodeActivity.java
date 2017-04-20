package com.eventx.moviex.TvActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieAdapter.CastAdapter;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.GuestStarAdapter;
import com.eventx.moviex.TvModels.Episodes;
import com.eventx.moviex.TvModels.GuestStars;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SingleEpisodeActivity extends AppCompatActivity {

    ImageView episodePoster;
    Toolbar toolbar;
    TextView episodeName;
    TextView episodeDate;
    TextView episodeOverview;
    RecyclerView guestStarList;
    ArrayList<GuestStars> guestStars;

    GuestStarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_episode);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Episodes episode = (Episodes) getIntent().getSerializableExtra("Episode");
        episodePoster = (ImageView) findViewById(R.id.episode_poster);
        episodeName = (TextView) findViewById(R.id.episode_name);
        episodeOverview = (TextView) findViewById(R.id.episode_overview);
        episodeDate = (TextView) findViewById(R.id.episode_date);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        guestStars=new ArrayList<>();
        guestStarList = (RecyclerView) findViewById(R.id.episode_guest_star);
        toolbar.setTitle(getIntent().getStringExtra("tvShow"));
        toolbar.setSubtitle("S"+episode.getSeason_number()+"E"+episode.getEpisode_number());
        setSupportActionBar(toolbar);


        guestStars.addAll(episode.getGuest_stars());

        adapter = new GuestStarAdapter(guestStars,this);
        guestStarList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + episode.getStill_path()).into(episodePoster);
        episodeName.setText(episode.getName());
        episodeOverview.setText(episode.getOverview());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if(episode.getAir_date()!=null) {
            try {
                date = sdfd.parse(episode.getAir_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            episodeDate.setText(sdf.format(date));
        }



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
