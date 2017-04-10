package com.eventx.moviex.MovieActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.eventx.moviex.MovieAdapter.VerticalMoviesAdapter;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesSearchResults extends AppCompatActivity implements VerticalMoviesAdapter.ListItemClickListener {
    RecyclerView searchResultList;
    ArrayList<Movie> searchResultMov;
    VerticalMoviesAdapter adapter;
    TextView searchTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movies_search_results);


        searchResultList = (RecyclerView) findViewById(R.id.search_movies_recycle_list);
        searchResultMov = new ArrayList<>();
        adapter = new VerticalMoviesAdapter(searchResultMov, this, this);
        searchTv=(TextView)findViewById(R.id.search_tv);
        searchTv.setText("Search Results for "+"\""+getIntent().getStringExtra("query")+"\"");
        searchResultList.setAdapter(adapter);

        searchResultMov.clear();
        fetchData(1);
    }

    private void fetchData(final int i) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        if (i > 5) {
            return;
        }


        Call<MovieResults> popularJson = apiInterface.getSearchResults(getIntent().getStringExtra("query"), i);
        popularJson.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful() && response.body().getResults().size() != 0) {
                    searchResultMov.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    fetchData(i + 1);
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }

        });

    }

    @Override
    public void itemClickListener(int clickedPosition) {
        Intent movieDetailsIntent = new Intent(MoviesSearchResults.this, MoviesDetailsActivity.class);
        movieDetailsIntent.putExtra("id", searchResultMov.get(clickedPosition).getMovieId());
        movieDetailsIntent.putExtra("title", searchResultMov.get(clickedPosition).getTitle());
        movieDetailsIntent.putExtra("poster",searchResultMov.get(clickedPosition).getPoster_path());
        startActivity(movieDetailsIntent);
    }
}
