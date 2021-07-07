package com.example.panshippingandroid.net;

import com.example.panshippingandroid.model.Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("data")
    Call<List<Data>> getData(
            @Query("id") int id,
            @Query("firstName") String firstName,
            @Query("lastName") String lastName,
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password

    );

    @GET("data")
    Call<List<Data>> getData();
}
