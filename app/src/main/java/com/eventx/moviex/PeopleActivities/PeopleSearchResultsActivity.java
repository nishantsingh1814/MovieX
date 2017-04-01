package com.eventx.moviex.PeopleActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleAdapter.PopularPeopleAdapter;
import com.eventx.moviex.PeopleModels.PopularPeople;
import com.eventx.moviex.PeopleModels.PopularResults;
import com.eventx.moviex.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleSearchResultsActivity extends AppCompatActivity implements PopularPeopleAdapter.ListItemClickListener {

    RecyclerView peopleSearchList;
    ArrayList<PopularPeople> searchPeople;
    PopularPeopleAdapter adapter;
    Button viewMore;
    int page=1;
    TextView searchTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_people_search_results);
        peopleSearchList = (RecyclerView) findViewById(R.id.people_search_list);
        searchPeople = new ArrayList<>();
        adapter = new PopularPeopleAdapter(searchPeople, this,this);
        peopleSearchList.setLayoutManager(new GridLayoutManager(this, 2));
        viewMore = (Button) findViewById(R.id.view_more);
        peopleSearchList.setAdapter(adapter);

        peopleSearchList.setNestedScrollingEnabled(false);
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData(page++);
            }
        });
        searchTv=(TextView)findViewById(R.id.search_tv);
        searchTv.setText("Search Results for "+"\""+getIntent().getStringExtra("query")+"\"");
        fetchData(page++);


    }

    private void fetchData(final int i) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<PopularResults> searchPeop = apiInterface.getSearchPeopleResults(getIntent().getStringExtra("query"),i);
        searchPeop.enqueue(new Callback<PopularResults>() {
            @Override
            public void onResponse(Call<PopularResults> call, Response<PopularResults> response) {
                if (response.isSuccessful()&&response.body().getResults().size()!=0) {

                    searchPeople.addAll(response.body().getResults());
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
    public void onListItemClick(int clickedPosition) {
        Intent tvShowDetailsIntent = new Intent(PeopleSearchResultsActivity.this, PeopleDetailsActivity.class);
        tvShowDetailsIntent.putExtra("id",searchPeople.get(clickedPosition).getId());
        tvShowDetailsIntent.putExtra("title", searchPeople.get(clickedPosition).getName());
        startActivity(tvShowDetailsIntent);
    }
}
