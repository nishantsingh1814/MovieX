package com.eventx.moviex.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nishant on 3/27/2017.
 */

public class ApiClient {

    static ApiInterface apiInterface;
    public static ApiInterface getApiInterface(){
        if(apiInterface==null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;

    }
}
