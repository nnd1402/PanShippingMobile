package com.example.panshippingandroid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.activities.MainActivity;
import com.example.panshippingandroid.adapters.AllProductAdapter;
import com.example.panshippingandroid.model.ProductDto;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;
import static com.example.panshippingandroid.utils.Const.AUTHENTICATION_FILE_NAME;
import static com.example.panshippingandroid.utils.Const.USER_ID;

public class AllProductsFragment extends Fragment {

    private AllProductAdapter allProductAdapter;
    private RecyclerView recyclerView;
    private List<ProductDto> list = new ArrayList<>();
    private TextView textView;
    private SharedPreferences sharedPreferences;
    private Long userId;
    private MainActivity activity;

    public static AllProductsFragment newInstance() {
        AllProductsFragment fragment = new AllProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AllProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MainActivity) requireActivity();
        sharedPreferences = requireContext().getSharedPreferences(AUTHENTICATION_FILE_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getLong(USER_ID, 0);
        initUI();
        getProductCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductCall();
    }

    private void initUI() {
        recyclerView = requireView().findViewById(R.id.all_recycleView);
        textView = requireView().findViewById(R.id.tv_null_list);
    }

    public void getProductCall() {
        activity.dialog.show();
        new Handler().postDelayed(() -> {
            if (activity.dialog.isShowing())
                activity.dialog.dismiss();
        }, 5000);
        //lista proizvoda ostalih user-a
        Call<List<ProductDto>> call = apiService.getAvailableToBuy(userId);
        call.enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductDto>> call, @NonNull Response<List<ProductDto>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    list.clear();

                    if (activity.dialog.isShowing())
                        activity.dialog.dismiss();

                    list = response.body();
                    if (list.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        if (activity.dialog.isShowing())
                            activity.dialog.dismiss();
                        textView.setVisibility(View.GONE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        allProductAdapter = new AllProductAdapter(getContext(), getParentFragmentManager(), list);
                        recyclerView.setAdapter(allProductAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductDto>> call, @NonNull Throwable t) {
                call.cancel();
                if (activity.dialog.isShowing())
                    activity.dialog.dismiss();
            }
        });
    }
}