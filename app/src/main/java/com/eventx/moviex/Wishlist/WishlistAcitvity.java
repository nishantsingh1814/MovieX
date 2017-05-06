package com.eventx.moviex.Wishlist;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.MenuItem;

import com.eventx.moviex.R;
import com.eventx.moviex.TvFragments.TvImagesFragment;

public class WishlistAcitvity extends AppCompatActivity {



    private ViewPager mViewPager;
    private TabLayout mTablayout;
    String sessionId;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wishlist_acitvity);
        sp=getSharedPreferences("MovieX",MODE_PRIVATE);
        sessionId=sp.getString("session",null);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse);
        collapsingToolbar.setTitleEnabled(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your Movies Watchlist");

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
                    getSupportActionBar().setTitle("Your Movies Watchlist");
                }else{
                    getSupportActionBar().setTitle("Your Tv Show Watchlist");
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

            if(sessionId!=null) {
                switch (position) {
                    case 0:
                        return new WatchlistFragment();
                    case 1:
                        return new WatchlistTvFragment();
                }
                return new TvImagesFragment();
            }else{
                switch (position) {
                    case 0:
                        return new WishlistMovieFrag();
                    case 1:
                        return new WishlistTvFragment();
                }
                return new TvImagesFragment();
            }
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
