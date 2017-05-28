package com.eventx.moviex.TvFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.MovieModels.EpisodeResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.SingleEpisodeActivity;
import com.eventx.moviex.TvAdapter.EpisodeAdapter;
import com.eventx.moviex.TvModels.Episodes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Nishant on 3/29/2017.
 */

public class TvEpisodeFragment extends Fragment implements EpisodeAdapter.ListItemClickListener {
    RecyclerView episodeList;
    ArrayList<Episodes> mEpisode;
    EpisodeAdapter adapter;

    int seasonNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tv_episode_fragment, container, false);
        mEpisode = new ArrayList<>();
        episodeList = (RecyclerView) v.findViewById(R.id.episode_list);
        adapter = new EpisodeAdapter(mEpisode, getContext(), this);
        episodeList.setAdapter(adapter);
        mEpisode.clear();
        seasonNumber = getArguments().getInt("season_number", -1);

        fetchData();
        return v;
    }

    public static TvEpisodeFragment newInstance(int exampleInt) {
        TvEpisodeFragment fragment = new TvEpisodeFragment();

        Bundle args = new Bundle();
        args.putInt("season_number", exampleInt);
        fragment.setArguments(args);

        return fragment;
    }


    private void fetchData() {

        ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null) {
            Call<EpisodeResults> results = apiInterface.getEpisodeResults(getActivity().getIntent().getLongExtra("id", -1), seasonNumber);

            results.enqueue(new Callback<EpisodeResults>() {
                @Override
                public void onResponse(Call<EpisodeResults> call, Response<EpisodeResults> response) {
                    if (response.isSuccessful()) {
                        mEpisode.addAll(response.body().getEpisodes());
                        adapter.notifyDataSetChanged();
                    } else {
                        fetchData();
                    }
                }

                @Override
                public void onFailure(Call<EpisodeResults> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        if (getActivity() != null) {
            Intent episodeDetailIntent = new Intent(getContext(), SingleEpisodeActivity.class);
            episodeDetailIntent.putExtra("tvShow", getActivity().getIntent().getStringExtra("title"));
            episodeDetailIntent.putExtra("Episode", mEpisode.get(clickedPosition));
            startActivity(episodeDetailIntent);
            getActivity().overridePendingTransition(R.anim.slide_right, R.anim.no_change);
        }
    }
}
