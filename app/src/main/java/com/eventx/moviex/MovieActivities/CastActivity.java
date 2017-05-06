package com.eventx.moviex.MovieActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieAdapter.CastVerticalAdapter;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastActivity extends AppCompatActivity {

    RecyclerView castList;
    CastVerticalAdapter adapter;
    ArrayList<Cast> cast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        castList = (RecyclerView) findViewById(R.id.cast_list);
        cast = new ArrayList<>();
        adapter = new CastVerticalAdapter(cast, this);
        castList.setAdapter(adapter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fetchData();

    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<CastResults> movieCast = apiInterface.getCast(getIntent().getLongExtra("id", -1));
        movieCast.enqueue(new Callback<CastResults>() {
            @Override
            public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Cast> castJson = response.body().getCast();
                    cast.clear();
                    cast.addAll(castJson);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CastResults> call, Throwable t) {

            }
        });
    }
}
