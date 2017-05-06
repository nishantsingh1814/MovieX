package com.eventx.moviex.MovieFragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.eventx.moviex.LoginActivity;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieActivities.MoviesSearchResults;
import com.eventx.moviex.MovieAdapter.UpcomingMoviesAdapter;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieActivities.MoviesButtonHandleActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieAdapter.HorizontalMoviesAdapter;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/27/2017.
 */

public class MoviesFrament extends Fragment {
    private RecyclerView moviesRecyclerList;
    private RecyclerView mNowShowingList;
    private RecyclerView mUpcomingList;
    private RecyclerView mTopRatedList;
    private HorizontalMoviesAdapter adapter;
    private ArrayList<Movie> mMovie;

    private UpcomingMoviesAdapter upcomingAdapter;
    private ArrayList<Movie> mUpcomingMovie;
    private HorizontalMoviesAdapter topRatedAdapter;
    private ArrayList<Movie> mTopRatedMovie;
    private HorizontalMoviesAdapter nowShowingadapter;
    private ArrayList<Movie> mNowshowingMovie;

    private Button mostPopularBtn;
    private Button topRatedBtn;
    private Button nowShowingBtn;
    private Button upcomingBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movies_fragment, container, false);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//            setupWindowAnimations();
//        }
        Log.i("poiuy", "onCreateMovies: ");

        mostPopularBtn = (Button) v.findViewById(R.id.popular);
        mostPopularBtn.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        mostPopularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Most Popular");
                startActivity(mostPopularIntent);
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.no_change);

            }
        });
        topRatedBtn = (Button) v.findViewById(R.id.top_rated);
        topRatedBtn.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        topRatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Top Rated");
                startActivity(mostPopularIntent);
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.no_change);

            }
        });
        nowShowingBtn = (Button) v.findViewById(R.id.now_Showing);
        nowShowingBtn.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        nowShowingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Now Showing");
                startActivity(mostPopularIntent);
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.no_change);

            }
        });
        upcomingBtn = (Button) v.findViewById(R.id.upcoming);
        upcomingBtn.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        upcomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPopularIntent = new Intent(getActivity(), MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button", "Upcoming");
                startActivity(mostPopularIntent);
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.no_change);
            }
        });
        mMovie = new ArrayList<>();
        mNowshowingMovie = new ArrayList<>();
        mUpcomingMovie = new ArrayList<>();
        mTopRatedMovie = new ArrayList<>();
        adapter = new HorizontalMoviesAdapter(mMovie, getContext());
        nowShowingadapter = new HorizontalMoviesAdapter(mNowshowingMovie, getContext());
        topRatedAdapter = new HorizontalMoviesAdapter(mTopRatedMovie, getContext());
        upcomingAdapter = new UpcomingMoviesAdapter(mUpcomingMovie, getContext());

        moviesRecyclerList = (RecyclerView) v.findViewById(R.id.movies_recycle_list);
        moviesRecyclerList.setNestedScrollingEnabled(false);
        moviesRecyclerList.setAdapter(adapter);

        mUpcomingList = (RecyclerView) v.findViewById(R.id.upcoming_list);
        mUpcomingList.setNestedScrollingEnabled(false);
        mNowShowingList = (RecyclerView) v.findViewById(R.id.now_showing_list);
        mNowShowingList.setNestedScrollingEnabled(false);
        mTopRatedList = (RecyclerView) v.findViewById(R.id.top_rated_list);
        mTopRatedList.setNestedScrollingEnabled(false);
        mUpcomingList.setAdapter(upcomingAdapter);
        mTopRatedList.setAdapter(topRatedAdapter);
        mNowShowingList.setAdapter(nowShowingadapter);
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
                } else {
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host")){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                    dialog.setMessage("Turn On mobile data/ wifi ");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fetchData();
                        }
                    });

                    dialog.create().show();
                }
            }
        });

        Call<MovieResults> nowShowingResults = apiInterface.getNowShowing();
        nowShowingResults.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> popularJson = response.body().getResults();
                    mNowshowingMovie.clear();
                    mNowshowingMovie.addAll(popularJson);
                    nowShowingadapter.notifyDataSetChanged();
                } else {
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
        Call<MovieResults> upcomingResults = apiInterface.getUpcoming("US");
        upcomingResults.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> popularJson = response.body().getResults();
                    mUpcomingMovie.clear();
                    mUpcomingMovie.addAll(popularJson);
                    upcomingAdapter.notifyDataSetChanged();
                } else {
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });

        Call<MovieResults> topRatedResults = apiInterface.getTopRated();
        topRatedResults.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> popularJson = response.body().getResults();
                    mTopRatedMovie.clear();
                    mTopRatedMovie.addAll(popularJson);
                    topRatedAdapter.notifyDataSetChanged();
                } else {
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
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

    }
}
