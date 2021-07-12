package com.example.panshippingandroid.api;

import com.example.panshippingandroid.model.UserModel;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<List<UserModel>> getAllUsers();

    @POST("addUser")
    @Headers({"Content-Type:application/json"})
    Call<Void> addUser(@Body UserModel userModel);

    @POST("Trades")
    @Headers({"Content-Type:application/json"})
    Call<Void> doTradeJob(@Body RequestBody json);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> insertUser(
            @Field("user") String firstName,
            @Field("password") String lastName);

}
