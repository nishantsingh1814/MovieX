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
import android.widget.TextView;

import com.eventx.moviex.MovieActivities.MoviesButtonHandleActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.TvActivities.TvButtonHandleActivity;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/30/2017.
 */

public class TvTonightFragment extends Fragment implements TvTonightAdapter.ListItemClickListener {
    RecyclerView tvTonightList;
    TvTonightAdapter adapter;
    ArrayList<TvShow> mTvTonight;
    TextView seeMore;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.now_showing_fragment,container,false);
        tvTonightList=(RecyclerView)v.findViewById(R.id.now_showing_list);
        mTvTonight=new ArrayList<>();
        adapter=new TvTonightAdapter(mTvTonight,getContext(),this);
        tvTonightList.setLayoutManager(new GridLayoutManager(getContext(),2));
        tvTonightList.setAdapter(adapter);
        seeMore=(TextView) v.findViewById(R.id.see_more);
        seeMore.setText("See All Shows Airing Tonight");
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mostPopularIntent=new Intent(getActivity(),TvButtonHandleActivity.class);
                mostPopularIntent.putExtra("button","Tv Tonight");
                startActivity(mostPopularIntent);
            }
        });
        fetchData();
        tvTonightList.setNestedScrollingEnabled(false);
        return v;
    }
    private void fetchData() {
        ApiInterface apiInterface= ApiClient.getApiInterface();

        Call<TvResults> popularJson = apiInterface.getTonightAir();
        popularJson.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful() && response.body().getResults().size() != 0) {
                    mTvTonight.clear();

                    for(int i=0;i<response.body().getResults().size()&&i<4;i++){
                        mTvTonight.add(response.body().getResults().get(i));
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {
            }
        });
    }

    @Override
    public void itemClickListener(int clickedPosition) {
        Intent tvDetailIntent = new Intent(getContext(), TvShowDetailsActivity.class);
        tvDetailIntent.putExtra("id",mTvTonight.get(clickedPosition).getTvId());
        tvDetailIntent.putExtra("title",mTvTonight.get(clickedPosition).getName());
        startActivity(tvDetailIntent);
    }
}
