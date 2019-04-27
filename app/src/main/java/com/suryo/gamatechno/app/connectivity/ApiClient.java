package com.suryo.gamatechno.app.connectivity;

import android.content.Context;

import com.suryo.gamatechno.app.others.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
