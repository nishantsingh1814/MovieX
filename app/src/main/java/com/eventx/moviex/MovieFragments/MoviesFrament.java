package com.eventx.moviex.MovieFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieActivities.MoviesSearchResults;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieActivities.MoviesButtonHandleActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieAdapter.HorizontalMoviesAdapter;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/27/2017.
 */

public class MoviesFrament extends Fragment implements HorizontalMoviesAdapter.ListItemClickListener {
    private RecyclerView moviesRecyclerList;
    private HorizontalMoviesAdapter adapter;
    private ArrayList<Movie> mMovie;
    private Button mostPopularBtn;
    private Button topRatedBtn;
    private Button nowShowingBtn;
    private Button upcomingBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movies_fragment, container, false);

        Log.i("poiuy", "onCreateMovies: ");

        mostPopularBtn = (Button) v.findViewById(R.id.popular);

        mostPopularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Most Popular");
                startActivity(mostPopularIntent);
            }
        });
        topRatedBtn = (Button) v.findViewById(R.id.top_rated);
        topRatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Top Rated");
                startActivity(mostPopularIntent);
            }
        });
        nowShowingBtn = (Button) v.findViewById(R.id.now_Showing);
        nowShowingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Now Showing");
                startActivity(mostPopularIntent);
            }
        });
        upcomingBtn = (Button) v.findViewById(R.id.upcoming);
        upcomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Upcoming");
                startActivity(mostPopularIntent);
            }
        });
        mMovie = new ArrayList<>();
        adapter = new HorizontalMoviesAdapter(mMovie, getContext(), this);
        moviesRecyclerList = (RecyclerView) v.findViewById(R.id.movies_recycle_list);
        moviesRecyclerList.setAdapter(adapter);
        setHasOptionsMenu(true);

        fetchData();
        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<MovieResults> popularResults = apiInterface.getPopularResults();
        popularResults.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> popularJson = response.body().getResults();
                    mMovie.clear();
                    mMovie.addAll(popularJson);
                    adapter.notifyDataSetChanged();
                }
                else {
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent movieDetailsIntent = new Intent(getActivity(), MoviesDetailsActivity.class);
        movieDetailsIntent.putExtra("id", mMovie.get(clickedPosition).getMovieId());
        movieDetailsIntent.putExtra("title", mMovie.get(clickedPosition).getTitle());
        movieDetailsIntent.putExtra("poster",mMovie.get(clickedPosition).getPoster_path());
        startActivity(movieDetailsIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search for movie...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchResultIntent = new Intent(getContext(), MoviesSearchResults.class);
                searchResultIntent.putExtra("query", query);
                startActivity(searchResultIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("poiuy", "onDestroyMovies: ");

    }
}
