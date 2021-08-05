package com.example.panshippingandroid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.adapters.ShippedProductAdapter;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.model.ProductShipping;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;
import static com.example.panshippingandroid.utils.Const.AUTHENTICATION_FILE_NAME;
import static com.example.panshippingandroid.utils.Const.USER_ID;


public class ShippedProductFragment extends Fragment {

    private ShippedProductAdapter buyProductAdapter;
    private RecyclerView recyclerView;
    private List<ProductDto> list = new ArrayList<>();
    private List<ProductShipping> shippingList = new ArrayList<>();
    private TextView textView;
    private SharedPreferences sharedPreferences;
    private Long userId;

    public static ShippedProductFragment newInstance() {
        ShippedProductFragment fragment = new ShippedProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ShippedProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(AUTHENTICATION_FILE_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getLong(USER_ID, 0);
        initUI();
        getShippedProductsCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        getShippedProductsCall();
    }

    private void initUI() {
        recyclerView = requireView().findViewById(R.id.buy_recycleView);
        textView = requireView().findViewById(R.id.tv_null_list);
    }

    public void getShippedProductsCall() {
        Call<List<ProductDto>> call = apiService.getBoughtProductsByUser(userId);
        call.enqueue(new Callback<List<ProductDto>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<List<ProductDto>> call, @NonNull Response<List<ProductDto>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    list.clear();

                    list = response.body();
                    if (list.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        buyProductAdapter = new ShippedProductAdapter(getContext(), getParentFragmentManager(), list);
                        recyclerView.setAdapter(buyProductAdapter);
                    }

                } else {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductDto>> call, @NonNull Throwable t) {
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}