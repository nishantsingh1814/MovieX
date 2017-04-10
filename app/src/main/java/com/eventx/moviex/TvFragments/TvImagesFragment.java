package com.eventx.moviex.TvFragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.ImageAdapter;
import com.eventx.moviex.TvModels.Poster;
import com.eventx.moviex.TvModels.PosterResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;

/**
 * Created by Nishant on 3/29/2017.
 */

public class TvImagesFragment extends Fragment {
    RecyclerView imageList;
    ArrayList<Poster> posters;

    ImageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.tv_image_fragment,container,false);

        imageList=(RecyclerView)v.findViewById(R.id.image_list);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageList.setLayoutManager(new GridLayoutManager(getContext(), 5));
        }else{
            imageList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        posters=new ArrayList<>();
        adapter=new ImageAdapter(posters,getContext());
        imageList.setAdapter(adapter);
        imageList.setNestedScrollingEnabled(false);

        fetchData();
        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface= ApiClient.getApiInterface();
        if(getActivity()!=null) {
            Call<PosterResult> postersResult = apiInterface.getImages(getActivity().getIntent().getLongExtra("id", -1));

            postersResult.enqueue(new Callback<PosterResult>() {
                @Override
                public void onResponse(Call<PosterResult> call, Response<PosterResult> response) {
                    if (response.isSuccessful()) {
                        posters.clear();
                        posters.addAll(response.body().getPosters());
                        adapter.notifyDataSetChanged();
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<PosterResult> call, Throwable t) {
                }
            });
        }
    }


}
