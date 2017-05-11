package com.eventx.moviex.favourite;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eventx.moviex.R;
import com.eventx.moviex.TvFragments.TvImagesFragment;
import com.eventx.moviex.Wishlist.WatchlistFragment;
import com.eventx.moviex.Wishlist.WatchlistTvFragment;

public class FavouriteActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_acitvity);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse);
        collapsingToolbar.setTitleEnabled(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Favourites Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        mTablayout.setupWithViewPager(mViewPager);


        mTablayout.getTabAt(0).setIcon(R.drawable.ic_movie_black_wish);
        mTablayout.getTabAt(1).setIcon(R.drawable.ic_tv_black_wish);
        mTablayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mTablayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#33CCCC"), PorterDuff.Mode.SRC_IN);

        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    getSupportActionBar().setTitle("Your Favourite Movies");
                }else{
                    getSupportActionBar().setTitle("Your Favourite Tv Shows");
                }
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    private class PageAdapter extends FragmentPagerAdapter {

        private String[] fragments = {"Movie", "Tv"};

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FavouriteMovieFragment();
                case 1:
                    return new FavouriteTvFragment();
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


}
