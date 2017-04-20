package com.eventx.moviex.TvActivities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eventx.moviex.MainActivity;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
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
import com.eventx.moviex.Wishlist.WishlistAcitvity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.activeandroid.Cache.getContext;

public class TvShowDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tv_show_details);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.no_change,R.anim.slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.similar_menu, menu);
        MenuItem settingsMenuItem = menu.findItem(R.id.get_similar);
        SpannableString s = new SpannableString(settingsMenuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        settingsMenuItem.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.get_similar) {

            Intent i = new Intent(TvShowDetailsActivity.this,TvImagesActivity.class );
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
        if(item.getItemId()==R.id.nav_wishlist){
            startActivity(new Intent(TvShowDetailsActivity.this, WishlistAcitvity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
