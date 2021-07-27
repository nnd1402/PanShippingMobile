package com.example.panshippingandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.panshippingandroid.R;

public class MyProductsFragment extends Fragment {

    public static final String TAG = "My products fragment";

    public static MyProductsFragment newInstance() {
        MyProductsFragment fragment = new MyProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_products, container, false);
    }
}