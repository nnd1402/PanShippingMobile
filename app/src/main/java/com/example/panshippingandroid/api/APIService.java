package com.example.panshippingandroid.api;

import com.example.panshippingandroid.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface APIService {

    @GET("user/{Id}")
    @Headers({"Content-Type:application/json"})
    Call getUserById(@Path("Id") int userId);


    @GET("getAllUsers")
    Call<List<UserModel>> getAllUsers();
}
