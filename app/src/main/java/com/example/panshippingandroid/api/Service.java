package com.example.panshippingandroid.api;

import android.content.Context;

import com.example.panshippingandroid.model.UserModel;
import com.example.panshippingandroid.utils.Const;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.panshippingandroid.utils.Const.BASE_URL;


public class Service {

    private static Retrofit retrofit;
    public static APIService apiClient;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIService getInstance() {
        if (apiClient == null) {
            apiClient = new APIService() {
                @Override
                public Call getUserById(int userId) {
                    return null;
                }

                @Override
                public Call<List<UserModel>> getAllUsers() {
                    return null;
                }

                @Override
                public Call<Void> addUser(UserModel userModel) {
                    return null;
                }

                @Override
                public Call<Void> doTradeJob(RequestBody json) {
                    return null;
                }

                @Override
                public Call<ResponseBody> insertUser(String firstName, String lastName) {
                    return null;
                }

            };
        }
        return apiClient;
    }

    public static Retrofit getInstance(Context context) {

        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okClient)
                .build();
    }

}
