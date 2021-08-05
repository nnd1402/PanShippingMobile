package com.example.panshippingandroid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.activities.MainActivity;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.model.ShippingModel;
import com.example.panshippingandroid.utils.Const;
import com.example.panshippingandroid.utils.ImageUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;
import static com.example.panshippingandroid.utils.Const.AUTHENTICATION_FILE_NAME;
import static com.example.panshippingandroid.utils.Const.USER_ID;

public class DetailsFragment extends Fragment {

    private ImageView productPictureIv;
    private TextView productNameTv;
    private TextView productPriceTv;
    private TextView descriptionTv;
    private Button buyProductBtn;
    private Long id;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = requireContext().getSharedPreferences(AUTHENTICATION_FILE_NAME, Context.MODE_PRIVATE);
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
                        productPriceTv.setText((product.getPrice()) + " €");
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

                        buyProductBtn.setOnClickListener(v -> {
                            Long userID = sharedPreferences.getLong(USER_ID, 0);
                            ShippingModel shipping = new ShippingModel();
                            shipping.setUserId(userID);
                            shipping.setProductId(product.getId());

                            addShippingProduct(shipping);
                            Toast.makeText(getActivity(), R.string.shipped_product, Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.shipped_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDto> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }


    public void addShippingProduct(ShippingModel shipping) {
        Call<Void> call = apiService.addShippingProducts(shipping);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.container, ShippedProductFragment.newInstance());
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), R.string.shipped_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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