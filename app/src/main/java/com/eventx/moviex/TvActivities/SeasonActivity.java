package com.eventx.moviex.TvActivities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.eventx.moviex.MovieModels.EpisodeResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleActivities.PeopleDetailsActivity;
import com.eventx.moviex.PeopleFragments.PeopleInfoFragment;
import com.eventx.moviex.PeopleFragments.PeopleMovieFragment;
import com.eventx.moviex.PeopleFragments.PeopleTvFragment;
import com.eventx.moviex.R;
import com.eventx.moviex.SingleImageActivity;
import com.eventx.moviex.TvFragments.TvEpisodeFragment;
import com.eventx.moviex.TvFragments.TvImagesFragment;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeasonActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTablayout;

    private ImageView mStillImage;
    private String poster_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse);
        collapsingToolbar.setTitleEnabled(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStillImage=(ImageView)findViewById(R.id.season_still_image);
        mStillImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageViewIntent=new Intent(SeasonActivity.this, SingleImageActivity.class);
                imageViewIntent.putExtra("image",poster_path);

                if (Build.VERSION.SDK_INT >= 21) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(SeasonActivity.this, view, "trans");
                    startActivity(imageViewIntent, options.toBundle());
                } else {
                    startActivity(imageViewIntent);
                }
            }
        });



        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        setImage(0);

        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                setImage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }
        });


    }

    private void setImage(int position) {
        ApiInterface apiInterface= ApiClient.getApiInterface();
        final Call<EpisodeResults> res=apiInterface.getEpisodeResults(getIntent().getLongExtra("id", -1), position+1);
        res.enqueue(new Callback<EpisodeResults>() {
            @Override
            public void onResponse(Call<EpisodeResults> call, Response<EpisodeResults> response) {
                if(response.isSuccessful()){
                    poster_path=response.body().getPoster_path();
                    Picasso.with(SeasonActivity.this).load("https://image.tmdb.org/t/p/w500"+response.body().getPoster_path()).into(mStillImage);
                }
            }

            @Override
            public void onFailure(Call<EpisodeResults> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class PageAdapter extends FragmentPagerAdapter {


        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TvEpisodeFragment fragment=TvEpisodeFragment.newInstance(position+1);
            return fragment;
        }

        @Override
        public int getCount() {
            return getIntent().getIntExtra("seasons",-1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Season "+(position+1);
        }
    }
}
