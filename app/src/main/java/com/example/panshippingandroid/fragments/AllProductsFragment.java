package com.example.panshippingandroid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.adapters.RecyclerViewAdapter;
import com.example.panshippingandroid.model.ProductDto;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;

public class AllProductsFragment extends Fragment {

    public static final String TAG = "All products fragment";
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<ProductDto> list = new ArrayList<>();
    private TextView textView;
    private ProgressBar progress;

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
        initUI();
        getProductCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductCall();
    }

    private void initUI() {
        recyclerView = requireView().findViewById(R.id.recycleView);
        textView = requireView().findViewById(R.id.tv_null_list);
        progress = requireView().findViewById(R.id.progress);
    }

    public void getProductCall() {
        Call<List<ProductDto>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductDto>> call, @NonNull Response<List<ProductDto>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    list.clear();

                    list = response.body();
                    if (list.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                        progress.setProgress(0);
                        progress.setSecondaryProgress(100);
                        progress.setMax(100);
                    } else {
                        progress.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), getParentFragmentManager(), list);

                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                    //Toast.makeText(getActivity(), R.string.successfully_added_product, Toast.LENGTH_SHORT).show();
//                    FragmentTransaction fr = getParentFragmentManager().beginTransaction();
//                    fr.replace(R.id.container, ViewProductsFragment.newInstance());
//                    fr.commit();
                } else {
                    //Toast.makeText(getActivity(), R.string.was_not_added_product, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductDto>> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }
}