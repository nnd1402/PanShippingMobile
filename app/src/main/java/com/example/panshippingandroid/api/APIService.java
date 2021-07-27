package com.example.panshippingandroid.api;

import com.example.panshippingandroid.model.LoginModel;
import com.example.panshippingandroid.model.ProductModel;
import com.example.panshippingandroid.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @GET("user/{Id}")
    @Headers({"Content-Type:application/json"})
    Call getUserById(@Path("Id") int userId);

    @GET("user")
    @Headers({"Content-Type:application/json"})
    Call<Void> getAllUsers();

    @POST("user/addUser")
    @Headers({"Content-Type:application/json"})
    Call<Void> addUser(@Body UserModel userModel);

    @POST("user/login")
    @Headers({"Content-Type:application/json"})
    Call<UserModel> login(@Body LoginModel loginModel);

    @POST("product/addProduct")
    @Headers({"Content-Type:application/json"})
    Call<Void> addProduct(@Body ProductModel productModel);
}
