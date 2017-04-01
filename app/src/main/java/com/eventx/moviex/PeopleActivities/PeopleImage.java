package com.eventx.moviex.PeopleActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleModels.PeopleImageResults;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.ImageAdapter;
import com.eventx.moviex.TvModels.Poster;
import com.eventx.moviex.TvModels.PosterResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleImage extends AppCompatActivity {

    RecyclerView imageList;
    ArrayList<Poster> images;

    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imageList = (RecyclerView) findViewById(R.id.image_list);
        imageList.setLayoutManager(new GridLayoutManager(this, 2));
        images = new ArrayList<>();
        adapter = new ImageAdapter(images, this);
        imageList.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();

        Call<PeopleImageResults> postersResult = apiInterface.getPeopleImages(getIntent().getLongExtra("id", -1));

        postersResult.enqueue(new Callback<PeopleImageResults>() {
            @Override
            public void onResponse(Call<PeopleImageResults> call, Response<PeopleImageResults> response) {
                if (response.isSuccessful()) {
                    images.clear();
                    images.addAll(response.body().getProfiles());
                    adapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onFailure(Call<PeopleImageResults> call, Throwable t) {
            }
        });

    }
}
