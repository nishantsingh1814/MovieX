package com.eventx.moviex.TvActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.VerticalTvAdapter;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarTvShowsActivity extends AppCompatActivity implements VerticalTvAdapter.ListItemClickListener {

    RecyclerView similarList;
    VerticalTvAdapter adapter;
    ArrayList<TvShow> mShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_similar_tv_shows);
        mShows = new ArrayList<>();
        adapter = new VerticalTvAdapter(mShows, this, this);
        similarList = (RecyclerView) findViewById(R.id.tv_similar_recycle_list);
        similarList.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<TvResults> similarTv = apiInterface.getTvSimilarResults(getIntent().getLongExtra("id", -1));
        similarTv.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> similarJson = response.body().getResults();
                    mShows.clear();
                    mShows.addAll(similarJson);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.i("hello", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {

            }
        });
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent tvShowDetailsIntent = new Intent(SimilarTvShowsActivity.this, TvShowDetailsActivity.class);
        tvShowDetailsIntent.putExtra("id", mShows.get(clickedPosition).getTvId());
        tvShowDetailsIntent.putExtra("title", mShows.get(clickedPosition).getName());
        startActivity(tvShowDetailsIntent);
    }

}
