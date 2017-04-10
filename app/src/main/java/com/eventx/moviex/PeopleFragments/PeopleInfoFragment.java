package com.eventx.moviex.PeopleFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleModels.PeopleInfo;
import com.eventx.moviex.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleInfoFragment extends Fragment {

    private TextView mBiography;
    private TextView mBirthday;
    private TextView mHomepage;
    private TextView mPlaceOfBirth;
    private TextView mKnownAs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.people_info_fragment, container, false);
        mBiography = (TextView) v.findViewById(R.id.biography);
        mBirthday = (TextView) v.findViewById(R.id.birthday);
        mHomepage = (TextView) v.findViewById(R.id.official_site);
        mPlaceOfBirth = (TextView) v.findViewById(R.id.place_of_birth);
        mKnownAs = (TextView) v.findViewById(R.id.also_known_as);

        fetchData();

        return v;
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null){
            Call<PeopleInfo> peopleInfo = apiInterface.getPeopleInfo(getActivity().getIntent().getLongExtra("id", -1));
        peopleInfo.enqueue(new Callback<PeopleInfo>() {
            @Override
            public void onResponse(Call<PeopleInfo> call, Response<PeopleInfo> response) {
                if (response.isSuccessful()) {
                    mBirthday.setText(response.body().getBirthday());
                    mBiography.setText(response.body().getBiography());
                    if (response.body().getHomepage() != null && !response.body().getHomepage().equals(""))
                        mHomepage.setText(response.body().getHomepage());
                    mPlaceOfBirth.setText(response.body().getPlace_of_birth());
                    if (response.body().getAlso_known_As() != null) {

                        for (int i = 0; i < response.body().getAlso_known_As().length; i++) {
                            if (i < response.body().getAlso_known_As().length - 1) {
                                if (i == 0) {
                                    mKnownAs.setText("");
                                }
                                mKnownAs.append(response.body().getAlso_known_As()[i] + " | ");
                            } else {
                                if (i == 0) {
                                    mKnownAs.setText("");
                                }
                                mKnownAs.append(response.body().getAlso_known_As()[i]);
                            }
                        }
                    }
                }
                else {
                    fetchData();
                }
            }

            @Override
            public void onFailure(Call<PeopleInfo> call, Throwable t) {

            }
        });
    }
    }
}
