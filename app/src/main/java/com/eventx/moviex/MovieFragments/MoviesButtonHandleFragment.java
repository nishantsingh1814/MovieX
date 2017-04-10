package com.eventx.moviex.MovieFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieAdapter.VerticalMoviesAdapter;
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

public class MoviesButtonHandleFragment extends Fragment implements VerticalMoviesAdapter.ListItemClickListener {
    RecyclerView mostPopList;
    ArrayList<Movie> movieBtnHandle;
    VerticalMoviesAdapter adapter;

    public void setListener(MovieClickListener listener) {
        this.listener = listener;
    }

    MovieClickListener listener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v=inflater.inflate(R.layout.movies_button_handle_frag,container,false);
        mostPopList = (RecyclerView)v. findViewById(R.id.most_popular_movies_recycle_list);
        movieBtnHandle = new ArrayList<>();
        adapter = new VerticalMoviesAdapter(movieBtnHandle, getActivity(),this);

        mostPopList.setAdapter(adapter);

        movieBtnHandle.clear();
        fetchData(1);
        return v;
    }

    private void fetchData(final int i) {
        ApiInterface apiInterface = ApiClient.getApiInterface();

        if (i > 5) {
            return;
        }

        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Upcoming")) {
            Call<MovieResults> popularJson = apiInterface.getUpcoming(i,"US");

            popularJson.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response.isSuccessful()) {
                        movieBtnHandle.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    }
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                }

            });
        }

        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Most Popular")) {
            Call<MovieResults> popularJson = apiInterface.getMostPop(i);

            popularJson.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response.isSuccessful()) {
                        movieBtnHandle.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    }
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                }

            });
        }
        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Top Rated")) {
            Call<MovieResults> popularJson = apiInterface.getTopRated(i);
            popularJson.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response.isSuccessful()) {
                        movieBtnHandle.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);

                    }
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                }
            });
        }
        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Now Showing")) {
            Call<MovieResults> popularJson = apiInterface.getNowShowing(i);
            popularJson.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response.isSuccessful() && response.body().getResults().size() != 0) {
                        movieBtnHandle.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    }
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                    Log.i("hello", "onResponse: " + t.getMessage());
                }
            });
        }
    }






    @Override
    public void itemClickListener(int clickedPosition) {
        listener.movieClickListener(movieBtnHandle.get(clickedPosition));
    }

    public interface MovieClickListener{
        void movieClickListener(Movie movieClass);
    }
}
