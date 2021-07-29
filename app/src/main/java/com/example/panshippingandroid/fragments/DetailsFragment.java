package com.example.panshippingandroid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.model.ProductModel;
import com.example.panshippingandroid.utils.ImageUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;

public class DetailsFragment extends Fragment {

    private ImageView productPictureIv;
    private TextView productNameTv;
    private TextView productPriceTv;
    private TextView descriptionTv;
    private Button buyProductBtn;
    private Long id;
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
        if (getArguments() != null) {
            id = getArguments().getLong("productId");
        }
        initUI();
        getProductCall(id);
        buyProductBtn.setOnClickListener(v -> {
            FragmentTransaction fr = getParentFragmentManager().beginTransaction();
            fr.replace(R.id.container, BuyProductFragment.newInstance());
            fr.commit();
        });
    }


    public void getProductCall(Long id) {
        Call<ProductDto> call = apiService.getProduct(id);
        call.enqueue(new Callback<ProductDto>() {
            @Override
            public void onResponse(@NonNull Call<ProductDto> call, @NonNull Response<ProductDto> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ProductDto product = response.body();
                    if (product != null) {
                        productNameTv.setText(product.getName());
                        productPriceTv.setText(String.valueOf(product.getPrice()));
                        descriptionTv.setText(product.getDescription());
                    }

                    if (product != null) {
                        if (product.getImage() != null) {
                            Glide.with(requireContext())
                                    .load(ImageUtils.convertStringImageToBitmap(product.getImage()))
                                    .override(1000, 600)
                                    .into(productPictureIv);
                        } else {
                            Glide.with(requireContext())
                                    .load(AppCompatResources.getDrawable(requireContext(), R.drawable.sale))
                                    .override(1000, 600)
                                    .into(productPictureIv);
                        }
                    }
                } else {
                    //Toast.makeText(getActivity(), R.string.was_not_added_product, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDto> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void initUI() {
        productPictureIv = requireView().findViewById(R.id.iv_detail);
        productNameTv = requireView().findViewById(R.id.tv_name_det);
        productPriceTv = requireView().findViewById(R.id.tv_price_det);
        descriptionTv = requireView().findViewById(R.id.tv_des_det);
        buyProductBtn = requireView().findViewById(R.id.buy_product_btn);
    }
}