package com.eventx.moviex.TvFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvActivity;
import com.eventx.moviex.TvActivities.TvButtonHandleActivity;
import com.eventx.moviex.TvActivities.TvSearchResults;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvAdapter.HorizontalTvAdapter;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/27/2017.
 */

public class TvFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView tvRecycleList;
    private HorizontalTvAdapter adapter;
    private HorizontalTvAdapter tvTonightadapter;
    private HorizontalTvAdapter topratedAdapter;

    private ArrayList<TvShow> mTopRated;
    private ArrayList<TvShow> mTvTonight;
    private ArrayList<TvShow> mTvShow;
    private Button mostPopularBtn;
    private Button topRatedButton;
    private Button tvTonightBtn;

    private RecyclerView topRatedRecyclerList;
    private RecyclerView tvTonightRecyclerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tv_fragment, container, false);
        mTvShow = new ArrayList<>();
        mTopRated=new ArrayList<>();
        mTvTonight=new ArrayList<>();

        tvTonightadapter=new HorizontalTvAdapter(mTvTonight,getActivity());
        topratedAdapter=new HorizontalTvAdapter(mTopRated,getActivity());


        adapter = new HorizontalTvAdapter(mTvShow, getActivity());
        tvRecycleList = (RecyclerView) v.findViewById(R.id.tv_show_recycle_list);
        tvRecycleList.setNestedScrollingEnabled(false);
        tvRecycleList.setAdapter(adapter);

        topRatedRecyclerList=(RecyclerView)v.findViewById(R.id.top_rated_recycle_list);
        topRatedRecyclerList.setNestedScrollingEnabled(false);
        topRatedRecyclerList.setAdapter(topratedAdapter);
        tvTonightRecyclerList=(RecyclerView)v.findViewById(R.id.air_today_recycle_list);
        tvTonightRecyclerList.setAdapter(tvTonightadapter);
        tvTonightRecyclerList.setNestedScrollingEnabled(false);

        mostPopularBtn = (Button) v.findViewById(R.id.tv_most_popular);
        mostPopularBtn.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        topRatedButton = (Button) v.findViewById(R.id.tv_top_rated);
        topRatedButton.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        tvTonightBtn = (Button) v.findViewById(R.id.tv_air_today);
        tvTonightBtn.setPaintFlags(mostPopularBtn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        mostPopularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tvButtonHandleIntent = new Intent(getActivity(), TvButtonHandleActivity.class);
                tvButtonHandleIntent.putExtra("button", "Most Popular");
                startActivity(tvButtonHandleIntent);
                getActivity().overridePendingTransition(R.anim.slide_right,R.anim.no_change);

            }
        });
        topRatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tvButtonHandleIntent = new Intent(getActivity(), TvButtonHandleActivity.class);
                tvButtonHandleIntent.putExtra("button", "Top Rated");
                startActivity(tvButtonHandleIntent);
                getActivity().overridePendingTransition(R.anim.slide_right,R.anim.no_change);

            }
        });
        tvTonightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tvButtonHandleIntent = new Intent(getActivity(), TvButtonHandleActivity.class);
                tvButtonHandleIntent.putExtra("button", "Tv Tonight");
                startActivity(tvButtonHandleIntent);
                getActivity().overridePendingTransition(R.anim.slide_right,R.anim.no_change);

            }
        });
        setHasOptionsMenu(true);

        fetchData();
        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<TvResults> popularResults = apiInterface.getPopularTvResults();
        popularResults.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> popularJson = response.body().getResults();
                    mTvShow.clear();
                    mTvShow.addAll(popularJson);
                    adapter.notifyDataSetChanged();
                }
                else{
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host")){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                    dialog.setMessage("Turn On mobile data/ wifi ");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fetchData();
                        }
                    });

                    dialog.create().show();
                }            }
        });
        Call<TvResults> tvTonightResults = apiInterface.getTonightAir();
        tvTonightResults.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> popularJson = response.body().getResults();
                    mTvTonight.clear();
                    mTvTonight.addAll(popularJson);
                    tvTonightadapter.notifyDataSetChanged();
                }
                else{
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {

            }
        });
        Call<TvResults> topRatedResults = apiInterface.getTopRatedTv();
        topRatedResults.enqueue(new Callback<TvResults>() {
            @Override
            public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> popularJson = response.body().getResults();
                    mTopRated.clear();
                    mTopRated.addAll(popularJson);
                    topratedAdapter.notifyDataSetChanged();
                }
                else{
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<TvResults> call, Throwable t) {

            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("poiuy", "onDestroyTv: ");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menu_search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setQueryHint("Search for Tv Show...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchResultIntent=new Intent(getContext(),TvSearchResults.class);
                searchResultIntent.putExtra("query",query);
                startActivity(searchResultIntent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


}
