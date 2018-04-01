package com.example.noone.curofy.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyRerotfitClient  {

    private static final String BASE_URL = "https://harsha.curofy.com/";
    private static Retrofit mRetrofit;

    private static Retrofit getRetrofitClient() {
        if (mRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return mRetrofit;
    }

    public static RetrofitService getRetrofitService() {

        Retrofit retrofit = getRetrofitClient();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        return retrofitService;
    }
}
