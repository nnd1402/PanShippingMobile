package com.example.panshippingandroid.api;

import com.example.panshippingandroid.utils.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Service {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
