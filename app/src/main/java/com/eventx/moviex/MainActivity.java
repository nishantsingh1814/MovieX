package com.eventx.moviex;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieFragments.MoviesFrament;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.PeopleFragments.PeopleFragment;
import com.eventx.moviex.TvActivities.TvActivity;
import com.eventx.moviex.TvActivities.TvSearchResults;
import com.eventx.moviex.TvFragments.TvEpisodeFragment;
import com.eventx.moviex.TvFragments.TvFragment;
import com.eventx.moviex.TvFragments.TvImagesFragment;
import com.eventx.moviex.TvFragments.TvShowInfoFragment;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.Wishlist.WishlistAcitvity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private NavigationView navigationView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse);
        collapsingToolbar.setTitleEnabled(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MovieX");
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));


        mTablayout.setupWithViewPager(mViewPager);


        mTablayout.getTabAt(0).setIcon(R.drawable.ic_movie_black_24dp);
        mTablayout.getTabAt(1).setIcon(R.drawable.ic_tv_black_24dp);
        mTablayout.getTabAt(2).setIcon(R.drawable.ic_perm_identity_black_24dp);
        mTablayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mTablayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#33CCCC"), PorterDuff.Mode.SRC_IN);
        mTablayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#33CCCC"), PorterDuff.Mode.SRC_IN);

        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(Color.parseColor("#33CCCC"), PorterDuff.Mode.SRC_IN);
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
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }
        if (item.getItemId() == R.id.nav_movie) {
            startActivity(new Intent(MainActivity.this, MoviesActivity.class));
        }
        if (item.getItemId() == R.id.nav_tv) {
            startActivity(new Intent(MainActivity.this, TvActivity.class));
        }
        if (item.getItemId() == R.id.nav_people) {
            startActivity(new Intent(MainActivity.this, PopularPeopleActivity.class));
        }
        if (item.getItemId() == R.id.nav_wishlist) {
            startActivity(new Intent(MainActivity.this, WishlistAcitvity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class PageAdapter extends FragmentPagerAdapter {

        private String[] fragments = {"Movie", "Tv", "Celebrities"};

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MoviesFrament();
                case 1:
                    return new TvFragment();
                case 2:
                    return new PeopleFragment();
            }
            return new TvImagesFragment();
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().findItem(R.id.nav_Home).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_movie).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_people).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_tv).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_wishlist).setChecked(false);
    }
}
