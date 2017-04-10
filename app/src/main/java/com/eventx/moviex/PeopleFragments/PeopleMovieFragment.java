package com.eventx.moviex.PeopleFragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieActivities.MoviesSearchResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleAdapter.PeopleMovieAdapter;
import com.eventx.moviex.PeopleModels.MovieCredits;
import com.eventx.moviex.PeopleModels.PeopleMovieCast;
import com.eventx.moviex.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleMovieFragment extends Fragment implements PeopleMovieAdapter.ListItemClickListener {
    private RecyclerView peopleMovieList;
    private ArrayList<MovieCredits> movieCreditses;
    private PeopleMovieAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.people_movie_fragment, container, false);
        peopleMovieList = (RecyclerView) v.findViewById(R.id.people_movie_list);
        movieCreditses = new ArrayList<>();
        adapter = new PeopleMovieAdapter(movieCreditses, getContext(), this);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            peopleMovieList.setLayoutManager(new GridLayoutManager(getContext(), 5));
        }else{
            peopleMovieList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        peopleMovieList.setAdapter(adapter);
        peopleMovieList.setNestedScrollingEnabled(false);
        fetchData();
        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null) {

            Call<PeopleMovieCast> movieCred = apiInterface.getMovieCredits(getActivity().getIntent().getLongExtra("id", -1));
            movieCred.enqueue(new Callback<PeopleMovieCast>() {
                @Override
                public void onResponse(Call<PeopleMovieCast> call, Response<PeopleMovieCast> response) {
                    if(response.isSuccessful()) {
                        movieCreditses.clear();
                        movieCreditses.addAll(response.body().getCast());
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        fetchData();
                    }
                }

                @Override
                public void onFailure(Call<PeopleMovieCast> call, Throwable t) {

                }
            });
        }
    }


    @Override
    public void onListItemClick(int clickedPosition) {
        Intent movieDetailsIntent = new Intent(getContext(), MoviesDetailsActivity.class);
        movieDetailsIntent.putExtra("id", movieCreditses.get(clickedPosition).getId());
        movieDetailsIntent.putExtra("title", movieCreditses.get(clickedPosition).getTitle());
        movieDetailsIntent.putExtra("poster",movieCreditses.get(clickedPosition).getPoster_path());
        startActivity(movieDetailsIntent);
    }
}
