package com.example.panshippingandroid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.panshippingandroid.R;

public class DetailsFragment extends Fragment {

    private ImageView iv_detail;
    private ImageView iv_buy;
    private TextView tv_name_det;
    private TextView tv_price_det;
    private EditText et_qua_det;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        iv_detail = requireView().findViewById(R.id.iv_detail);
        iv_buy = requireView().findViewById(R.id.iv_buy);
        tv_name_det = requireView().findViewById(R.id.tv_name_det);
        tv_price_det = requireView().findViewById(R.id.tv_price_det);
        et_qua_det = requireView().findViewById(R.id.et_qua_det);
    }
}