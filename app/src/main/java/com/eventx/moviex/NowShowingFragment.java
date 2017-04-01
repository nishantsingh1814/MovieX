package com.eventx.moviex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eventx.moviex.MovieActivities.MoviesButtonHandleActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/30/2017.
 */

public class NowShowingFragment extends Fragment implements NowShowingAdapter.ListItemClickListener {
    RecyclerView nowShowingList;
    ArrayList<Movie> mMovie;
    NowShowingAdapter adapter;

    TextView seeMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.now_showing_fragment,container,false);
        nowShowingList=(RecyclerView)v.findViewById(R.id.now_showing_list);
        mMovie=new ArrayList<>();
        adapter=new NowShowingAdapter(mMovie,getContext(),this);
        nowShowingList.setLayoutManager(new GridLayoutManager(getContext(),2));
        nowShowingList.setAdapter(adapter);
        seeMore=(TextView) v.findViewById(R.id.see_more);
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mostPopularIntent=new Intent(getActivity(),MoviesButtonHandleActivity.class);
                mostPopularIntent.putExtra("button","Now Showing");
                startActivity(mostPopularIntent);
            }
        });
        nowShowingList.setNestedScrollingEnabled(false);

        fetchData();
        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface= ApiClient.getApiInterface();

        Call<MovieResults> popularJson = apiInterface.getNowShowing();
        popularJson.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful() && response.body().getResults().size() != 0) {
                    mMovie.clear();

                    for(int i=0;i<response.body().getResults().size()&&i<4;i++){
                        mMovie.add(response.body().getResults().get(i));
                    }
                    Log.i("hello", "onResponse: "+mMovie.size());

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }
        });
    }

    @Override
    public void itemClickListener(int clickedPosition) {
        Intent moviesDetailIntent = new Intent(getContext(), MoviesDetailsActivity.class);
        moviesDetailIntent.putExtra("id",mMovie.get(clickedPosition).getMovieId());
        moviesDetailIntent.putExtra("title",mMovie.get(clickedPosition).getTitle());
        startActivity(moviesDetailIntent);
    }
}
