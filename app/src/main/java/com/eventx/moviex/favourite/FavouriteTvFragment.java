package com.eventx.moviex.favourite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.WishlistAdapter.WatchlistTvAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 4/25/2017.
 */

public class FavouriteTvFragment extends Fragment {

    private ArrayList<TvShow> mShow;

    private RecyclerView mList;
    FavouriteTvAdapter adapter;

    SharedPreferences sp;
    String sessionId;
    long accountId;

    private int resume;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wishlist_movie_frag, container, false);

        sp = getActivity().getSharedPreferences("MovieX", Context.MODE_PRIVATE);

        sessionId = sp.getString("session", null);
        accountId = sp.getLong("account", -1);
        mShow = new ArrayList<>();

        mList = (RecyclerView) v.findViewById(R.id.recycler_view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mList.setLayoutManager(new GridLayoutManager(getContext(), 5));
        } else {
            mList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        adapter = new FavouriteTvAdapter(mShow, getContext());
        mList.setAdapter(adapter);
        mShow.clear();
        fetchData(1);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (resume++ != 0) {
            mShow.clear();
            fetchData(1);
        }
    }

    private void fetchData(final int i) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<TvResults> movieResultsCall = apiInterface.getFavoriteTV(accountId, sessionId, i);
        movieResultsCall.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> temp = new ArrayList<>();
                    temp.addAll(response.body().getResults());
                    if (temp.size() == 20) {
                        mShow.addAll(temp);
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    } else if (temp.size() < 20) {
                        mShow.addAll(temp);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {

            }
        });

    }
}
