package com.eventx.moviex.TvFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvAdapter.VerticalTvAdapter;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/27/2017.
 */

public class TvbuttonHandleFragment extends Fragment implements VerticalTvAdapter.ListItemClickListener {
    RecyclerView mHandleList;
    ArrayList<TvShow> mHandleTv;
    VerticalTvAdapter adapter;

    public void setVerticalTvClickListener(VerticalTvClickListener verticalTvClickListener) {
        this.verticalTvClickListener = verticalTvClickListener;
    }

    VerticalTvClickListener verticalTvClickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tv_button_handle_frag,container,false);

        mHandleList = (RecyclerView) v.findViewById(R.id.tv_btn_recycle_list);
        mHandleTv = new ArrayList<>();
        adapter = new VerticalTvAdapter(mHandleTv,getContext(), this);

        mHandleList.setAdapter(adapter);
        mHandleTv.clear();

        fetchData(1);
        return v;
    }
    private void fetchData(final int i) {
        ApiInterface apiInterface = ApiClient.getApiInterface();

        if(i>5){
            return;
        }
        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Most Popular")) {

            Call<TvResults> popularJson = apiInterface.getMostPopTv(i);
            popularJson.enqueue(new Callback<TvResults>() {
                @Override
                public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                    if (response.isSuccessful() && response.body().getResults().size() != 0) {
                        mHandleTv.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    }
                }

                @Override
                public void onFailure(Call<TvResults> call, Throwable t) {
                }

            });

        }
        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Top Rated")) {
            Call<TvResults> topRatedJson = apiInterface.getTopRatedTv(i);
            topRatedJson.enqueue(new Callback<TvResults>() {
                @Override
                public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                    if (response.isSuccessful() && response.body().getResults().size() != 0) {
                        mHandleTv.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    }
                }

                @Override
                public void onFailure(Call<TvResults> call, Throwable t) {

                }
            });

        }
        if (getActivity()!=null&&getActivity().getIntent().getStringExtra("button").equals("Tv Tonight")) {
            Call<TvResults> tonightJson = apiInterface.getTonightAir(i);
            tonightJson.enqueue(new Callback<TvResults>() {
                @Override
                public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                    if (response.isSuccessful() && response.body().getResults().size() != 0) {
                        mHandleTv.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        fetchData(i + 1);
                    }
                }

                @Override
                public void onFailure(Call<TvResults> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void onListItemClick(int clickedPosition) {
        verticalTvClickListener.verticalTvClick(mHandleTv.get(clickedPosition));
    }

    public interface VerticalTvClickListener{
        void verticalTvClick(TvShow show);
    }
}
