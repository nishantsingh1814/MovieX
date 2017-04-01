package com.eventx.moviex.TvActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eventx.moviex.MainActivity;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.R;
import com.eventx.moviex.TvFragments.TvEpisodeFragment;
import com.eventx.moviex.TvFragments.TvFragment;
import com.eventx.moviex.TvFragments.TvImagesFragment;
import com.eventx.moviex.TvFragments.TvShowInfoFragment;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTablayout;

    private ImageView mBackdropImage;
    private ImageButton mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        mBackdropImage = (ImageView) findViewById(R.id.backdrop_image);
        mPlayButton = (ImageButton) findViewById(R.id.play_trailer);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<ResultTrailer> keyResult = apiInterface.getTvTrailerKey(getIntent().getLongExtra("id", -1));
                keyResult.enqueue(new Callback<ResultTrailer>() {
                    @Override
                    public void onResponse(Call<ResultTrailer> call, Response<ResultTrailer> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResults().size() == 0) {
                                Snackbar.make(view, "No trailer Available", Snackbar.LENGTH_LONG).show();
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

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fetchData();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.similar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.get_similar) {

            Intent i = new Intent(TvShowDetailsActivity.this, SimilarTvShowsActivity.class);
            i.putExtra("id", getIntent().getLongExtra("id", -1));
            startActivity(i);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.nav_Home){
            startActivity(new Intent(TvShowDetailsActivity.this, MainActivity.class));
        }
        if (item.getItemId() == R.id.nav_movie) {
            startActivity(new Intent(TvShowDetailsActivity.this, MoviesActivity.class));
        }
        if (item.getItemId() == R.id.nav_tv) {
            startActivity(new Intent(TvShowDetailsActivity.this, TvActivity.class));
        }
        if(item.getItemId()==R.id.nav_people){
            startActivity(new Intent(TvShowDetailsActivity.this, PopularPeopleActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<TvShow> tvShowDetails = apiInterface.getTvShowDetails(getIntent().getLongExtra("id", -1));
        tvShowDetails.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                if (response.isSuccessful()) {
                    Picasso.with(TvShowDetailsActivity.this).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                }
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {

            }
        });
    }

    private class PageAdapter extends FragmentPagerAdapter {

        private String[] fragments = {"Info", "Episodes", "Pictures"};

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TvShowInfoFragment();
                case 1:
                    return new TvEpisodeFragment();
                case 2:
                    return new TvImagesFragment();
            }
            return new TvImagesFragment();
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }
}
