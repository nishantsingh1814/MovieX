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

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleAdapter.PeopleMovieAdapter;
import com.eventx.moviex.PeopleAdapter.PeopleTvAdapter;
import com.eventx.moviex.PeopleModels.MovieCredits;
import com.eventx.moviex.PeopleModels.PeopleMovieCast;
import com.eventx.moviex.PeopleModels.PeopleTvCast;
import com.eventx.moviex.PeopleModels.TvCredits;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvSearchResults;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleTvFragment extends Fragment implements PeopleTvAdapter.ListItemClickListener {
    private RecyclerView peopleTvList;
    private ArrayList<TvCredits> tvCreditses;
    private PeopleTvAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.people_movie_fragment, container, false);
        peopleTvList = (RecyclerView) v.findViewById(R.id.people_movie_list);
        tvCreditses = new ArrayList<>();
        adapter = new PeopleTvAdapter(tvCreditses, getContext(), this);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            peopleTvList.setLayoutManager(new GridLayoutManager(getContext(), 5));
        }else{
            peopleTvList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        peopleTvList.setAdapter(adapter);
        peopleTvList.setNestedScrollingEnabled(false);
        fetchData();
        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null) {

            Call<PeopleTvCast> movieCred = apiInterface.getTvCredits(getActivity().getIntent().getLongExtra("id", -1));
            movieCred.enqueue(new Callback<PeopleTvCast>() {
                @Override
                public void onResponse(Call<PeopleTvCast> call, Response<PeopleTvCast> response) {
                    if (response.isSuccessful()) {
                        tvCreditses.clear();

                        tvCreditses.addAll(response.body().getCast());
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        fetchData();
                    }
                }

                @Override
                public void onFailure(Call<PeopleTvCast> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent tvShowDetailsIntent = new Intent(getContext(), TvShowDetailsActivity.class);
        tvShowDetailsIntent.putExtra("id", tvCreditses.get(clickedPosition).getId());
        tvShowDetailsIntent.putExtra("title", tvCreditses.get(clickedPosition).getName());
        tvShowDetailsIntent.putExtra("poster",tvCreditses.get(clickedPosition).getPoster_path());
        startActivity(tvShowDetailsIntent);
    }
}
