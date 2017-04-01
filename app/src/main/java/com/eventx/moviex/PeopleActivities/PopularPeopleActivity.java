package com.eventx.moviex.PeopleActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.eventx.moviex.MainActivity;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleAdapter.PopularPeopleAdapter;
import com.eventx.moviex.PeopleModels.PopularPeople;
import com.eventx.moviex.PeopleModels.PopularResults;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvActivity;
import com.eventx.moviex.TvActivities.TvSearchResults;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularPeopleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopularPeopleAdapter.ListItemClickListener {

    PopularPeopleAdapter adapter;
    RecyclerView popularPeopleList;
    ArrayList<PopularPeople> peoples;
    Button viewMore;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Popular Celebrities");

        popularPeopleList = (RecyclerView) findViewById(R.id.popular_people_list);
        peoples = new ArrayList<>();
        peoples.clear();
        popularPeopleList.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new PopularPeopleAdapter(peoples, this,this);
        popularPeopleList.setAdapter(adapter);
        viewMore = (Button) findViewById(R.id.view_more);
        viewMore.setVisibility(View.INVISIBLE);

        popularPeopleList.setNestedScrollingEnabled(false);

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData(page++);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fetchData(page++);


    }

    private void fetchData(final int i) {

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<PopularResults> popularPeop = apiInterface.getPopularPeople(i);
        popularPeop.enqueue(new Callback<PopularResults>() {
            @Override
            public void onResponse(Call<PopularResults> call, Response<PopularResults> response) {
                if (response.isSuccessful()) {

                    peoples.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    if (i == 1)
                        viewMore.setVisibility(View.VISIBLE);

                } else {
                    viewMore.setVisibility(View.INVISIBLE);
                    return;
                }

            }

            @Override
            public void onFailure(Call<PopularResults> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menu_search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchResultIntent=new Intent(PopularPeopleActivity.this,PeopleSearchResultsActivity.class);
                searchResultIntent.putExtra("query",query);
                startActivity(searchResultIntent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
            startActivity(new Intent(PopularPeopleActivity.this, MainActivity.class));
        }
        if (item.getItemId() == R.id.nav_movie) {
            startActivity(new Intent(PopularPeopleActivity.this, MoviesActivity.class));
        }
        if (item.getItemId() == R.id.nav_tv) {
            startActivity(new Intent(PopularPeopleActivity.this, TvActivity.class));

        }
        if (item.getItemId() == R.id.nav_people) {
            startActivity(new Intent(PopularPeopleActivity.this, PopularPeopleActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListItemClick(int clickedPosition) {
        Intent peopleDetailIntent = new Intent(PopularPeopleActivity.this, PeopleDetailsActivity.class);
        peopleDetailIntent.putExtra("id", peoples.get(clickedPosition).getId());
        peopleDetailIntent.putExtra("title", peoples.get(clickedPosition).getName());
        startActivity(peopleDetailIntent);
    }
}
