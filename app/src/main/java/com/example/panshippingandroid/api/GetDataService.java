package com.example.panshippingandroid.api;

import com.example.panshippingandroid.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("getUser")
    Call<UserModel> getUserById(
            @Query("id") int id
    );

    @GET("getAllUsers")
    Call<List<UserModel>> getAllUsers();
}
