package com.example.panshippingandroid.api;

import com.example.panshippingandroid.model.LoginModel;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.model.ProductModel;
import com.example.panshippingandroid.model.ProductShipping;
import com.example.panshippingandroid.model.ShippingModel;
import com.example.panshippingandroid.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("product")
    @Headers({"Content-Type:application/json"})
    Call<List<ProductDto>> getAllProducts();

    @GET("product/getAvailableToBuy/{id}")
    @Headers({"Content-Type:application/json"})
    Call<List<ProductDto>> getAvailableToBuy(@Path("id") Long id);

    @GET("product/getProductsByUser/{id}")
    @Headers({"Content-Type:application/json"})
    Call<List<ProductDto>> getMyProducts(@Path("id") Long id);

    @GET("product/{id}")
    @Headers({"Content-Type:application/json"})
    Call<ProductDto> getProduct(@Path("id") Long id);

    @DELETE("product/{id}")
    @Headers({"Content-Type:application/json"})
    Call<Void> deleteProduct(@Path("id") Long id);

    @PUT("product/{id}")
    @Headers({"Content-Type:application/json"})
    Call<Void> editProduct(@Path("id") Long id, @Body ProductModel productModel);

    @POST("shipping/addShipment")
    @Headers({"Content-Type:application/json"})
    Call<Void> addShippingProducts(@Body ShippingModel shipping);

    @GET("product/getBoughtProductsByUser/{userId}")
    @Headers({"Content-Type:application/json"})
    Call<List<ProductDto>> getBoughtProductsByUser(@Path("userId") Long userId);

}
