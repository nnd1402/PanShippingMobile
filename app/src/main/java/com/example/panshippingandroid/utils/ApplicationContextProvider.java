package com.example.panshippingandroid.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class ApplicationContextProvider extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}