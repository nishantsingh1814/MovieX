package com.eventx.moviex.TvFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.TvCastAdapter;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.TvModels.TvShowDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/29/2017.
 */

public class TvShowInfoFragment extends Fragment {
    TextView tvGenre;
    TextView overviewTv;
    TextView mTvRatings;
    TextView mReleaseTv;

    RecyclerView tv_show_cast;
    TvCastAdapter adapter;
    ArrayList<Cast> mCast;

    TvShowDetails mShow;

//    RecyclerView mMoreLikeThis;
//    ArrayList<TvShow> similar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.tv_show_info_fragment,container,false);
        tvGenre=(TextView)v.findViewById(R.id.tv_genre);
        overviewTv=(TextView)v.findViewById(R.id.tv_show_overview);
        mTvRatings=(TextView)v.findViewById(R.id.show_ratings);
        mReleaseTv=(TextView)v.findViewById(R.id.year_started);
        mCast=new ArrayList<>();
        adapter=new TvCastAdapter(mCast,getContext());
        tv_show_cast=(RecyclerView)v.findViewById(R.id.tv_cast_recycle_list);
        tv_show_cast.setAdapter(adapter);
        mShow=new TvShowDetails();

        fetchData();

        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        if(getActivity()!=null) {
            Call<TvShowDetails> tvShowDetails = apiInterface.getSingleTvShowDetails(getActivity().getIntent().getLongExtra("id", -1));

            tvShowDetails.enqueue(new Callback<TvShowDetails>() {
                @Override
                public void onResponse(Call<TvShowDetails> call, Response<TvShowDetails> response) {
                    if (response.isSuccessful()) {
                        mShow = response.body();
                        for (int i = 0; i < mShow.getGenres().size(); i++) {
                            if (i < mShow.getGenres().size() - 1) {
                                tvGenre.append(mShow.getGenres().get(i).getName() + "|");
                            } else {
                                tvGenre.append(mShow.getGenres().get(i).getName());
                            }
                        }

                        overviewTv.setText(mShow.getOverview());
                        mTvRatings.setText(mShow.getVote_average() + " (" + mShow.getVote_count() + ")");
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

                        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        if(mShow.getFirst_air_date()!=null) {
                            try {
                                date = sdfd.parse(mShow.getFirst_air_date());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mReleaseTv.setText(sdf.format(date));
                        }

                    }
                }

                @Override
                public void onFailure(Call<TvShowDetails> call, Throwable t) {

                }
            });
        }
        if(getActivity()!=null) {

            Call<CastResults> castDetails = apiInterface.getTvCast(getActivity().getIntent().getLongExtra("id", -1));
            castDetails.enqueue(new Callback<CastResults>() {
                @Override
                public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                    if (response.isSuccessful()) {

                        ArrayList<Cast> castJson = response.body().getCast();
                        Log.i("hello", "onResponse: " + castJson.size());
                        mCast.clear();
                        mCast.addAll(castJson);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<CastResults> call, Throwable t) {

                }
            });
        }

    }

}
