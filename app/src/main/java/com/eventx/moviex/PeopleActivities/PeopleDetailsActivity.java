package com.eventx.moviex.PeopleActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.eventx.moviex.MainActivity;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleFragments.PeopleInfoFragment;
import com.eventx.moviex.PeopleFragments.PeopleMovieFragment;
import com.eventx.moviex.PeopleFragments.PeopleTvFragment;
import com.eventx.moviex.PeopleModels.PeopleInfo;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvActivity;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvFragments.TvEpisodeFragment;
import com.eventx.moviex.TvFragments.TvImagesFragment;
import com.eventx.moviex.TvFragments.TvShowInfoFragment;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;
import static android.R.attr.start;

public class PeopleDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTablayout;

    private ImageView mProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse);
        collapsingToolbar.setTitleEnabled(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        mProfileImage = (ImageView) findViewById(R.id.people_profile_image);


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

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<PeopleInfo> peopleInfo = apiInterface.getPeopleInfo(getIntent().getLongExtra("id", -1));
        peopleInfo.enqueue(new Callback<PeopleInfo>() {
            @Override
            public void onResponse(Call<PeopleInfo> call, Response<PeopleInfo> response) {
                if (response.isSuccessful()) {
                    Picasso.with(PeopleDetailsActivity.this).load("https://image.tmdb.org/t/p/w500" + response.body().getProfile_path()).into(mProfileImage);
                }
            }

            @Override
            public void onFailure(Call<PeopleInfo> call, Throwable t) {

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
            startActivity(new Intent(PeopleDetailsActivity.this, MainActivity.class));
        }
        if (item.getItemId() == R.id.nav_movie) {
            startActivity(new Intent(PeopleDetailsActivity.this, MoviesActivity.class));
        }
        if (item.getItemId() == R.id.nav_tv) {
            startActivity(new Intent(PeopleDetailsActivity.this, TvActivity.class));
        }
        if(item.getItemId()==R.id.nav_people){
            startActivity(new Intent(PeopleDetailsActivity.this, PopularPeopleActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class PageAdapter extends FragmentPagerAdapter {

        private String[] fragments = {"Info", "Movies", "Tv Shows"};

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PeopleInfoFragment();
                case 1:
                    return new PeopleMovieFragment();
                case 2:
                    return new PeopleTvFragment();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.people_image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.people_images){
            Intent imageIntent=new Intent(this,PeopleImage.class);
            imageIntent.putExtra("id",getIntent().getLongExtra("id", -1));
            startActivity(imageIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}