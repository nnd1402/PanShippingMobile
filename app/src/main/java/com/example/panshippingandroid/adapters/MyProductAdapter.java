package com.example.panshippingandroid.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.fragments.AddProductFragment;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    private final Context context;
    private List<ProductDto> productModels;
    private FragmentManager fragmentManager;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public Long userId;

    public MyProductAdapter(Context context, FragmentManager fragmentManager, List<ProductDto> productDtoList, Long userId) {
        this.context = context;
        this.productModels = productDtoList;
        this.fragmentManager = fragmentManager;
        this.userId = userId;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public MyProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_single_item, parent, false);
        return new MyProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.MyViewHolder holder, int position) {

        holder.tv_name.setText(productModels.get(position).getName());
        holder.tv_price.setText(String.valueOf(productModels.get(position).getPrice()) + " €");
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(productModels.get(position).getId()));

        if (productModels.get(position).getImage() != null) {
            Glide.with(context)
                    .load(ImageUtils.convertStringImageToBitmap(productModels.get(position).getImage()))
                    .override(400, 400)
                    .into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(AppCompatResources.getDrawable(context, R.drawable.sale))
                    .override(400, 400)
                    .into(holder.imageView);
        }

        holder.btn_edit.setOnClickListener(v -> {
            replaceFragment(new AddProductFragment(), productModels.get(position).getId());
        });

        holder.btn_delete.setOnClickListener(v ->
                new AlertDialog.Builder(context).setTitle("Do you want to delete a product?")
                        .setPositiveButton("Yes", (dialog, which) ->
                                MyProductAdapter
                                        .this
                                        .deleteProduct(productModels.get(position).getId()))
                        .setNegativeButton("No", null)
                        .show());
    }

    public void getProductCall() {
        Call<List<ProductDto>> call = apiService.getMyProducts(userId);
        call.enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductDto>> call, @NonNull Response<List<ProductDto>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    productModels.clear();
                    productModels = response.body();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductDto>> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void deleteProduct(Long id) {
        Call<Void> call = apiService.deleteProduct(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Toast.makeText(context.getApplicationContext(), "Product deleted!", Toast.LENGTH_SHORT).show();
                    getProductCall();
                } else {
                    if (response.errorBody() != null) {
                        Toast.makeText(context.getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    private void replaceFragment(Fragment fragment, Long id) {
        Bundle args = new Bundle();
        args.putBoolean("isEdit", true);
        args.putLong("productId", id);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final TextView tv_price;
        private final ImageView imageView;
        private final ImageView btn_edit;
        private final ImageView btn_delete;
        public SwipeRevealLayout swipeRevealLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.rv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            imageView = itemView.findViewById(R.id.rv_image);
            swipeRevealLayout = itemView.findViewById(R.id.swipe);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}



