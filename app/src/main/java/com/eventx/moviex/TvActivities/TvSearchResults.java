package com.eventx.moviex.TvActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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

public class TvSearchResults extends AppCompatActivity implements VerticalTvAdapter.ListItemClickListener {

    RecyclerView tvSearchResultList;
    ArrayList<TvShow> searchResultTv;
    VerticalTvAdapter adapter;
    private TextView searchTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tv_serach_results);
        tvSearchResultList = (RecyclerView) findViewById(R.id.search_tv_recycle_list);
        searchResultTv = new ArrayList<>();
        adapter = new VerticalTvAdapter(searchResultTv, this, this);

        searchTv=(TextView)findViewById(R.id.search_tv);
        searchTv.setText("Search Results for "+"\""+getIntent().getStringExtra("query")+"\"");


        tvSearchResultList.setAdapter(adapter);
        searchResultTv.clear();
        fetchData(1);
    }

    private void fetchData(final int i) {
        ApiInterface apiInterface = ApiClient.getApiInterface();

        if (i > 5) {
            return;
        }
        Call<TvResults> searchJson = apiInterface.getTvSearchResults(getIntent().getStringExtra("query"), i);
        searchJson.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful() && response.body().getResults().size() != 0) {
                    searchResultTv.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    fetchData(i + 1);
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {

            }
        });

    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent tvShowDetailsIntent = new Intent(TvSearchResults.this, TvShowDetailsActivity.class);
        tvShowDetailsIntent.putExtra("id", searchResultTv.get(clickedPosition).getTvId());
        tvShowDetailsIntent.putExtra("title", searchResultTv.get(clickedPosition).getName());
        startActivity(tvShowDetailsIntent);
    }
}
