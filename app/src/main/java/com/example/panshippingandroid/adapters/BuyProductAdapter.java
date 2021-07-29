package com.example.panshippingandroid.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.fragments.DetailsFragment;
import com.example.panshippingandroid.model.ProductDto;
import com.example.panshippingandroid.utils.ImageUtils;

import java.util.List;

public class BuyProductAdapter  extends RecyclerView.Adapter<BuyProductAdapter.MyViewHolder> {

    private final Context context;
    private List<ProductDto> productModels;
    private FragmentManager fragmentManager;

    public BuyProductAdapter(Context context, FragmentManager fragmentManager, List<ProductDto> productDtoList) {
        this.context = context;
        this.productModels = productDtoList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public BuyProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_single_no_swipe_item, parent, false);
        return new BuyProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyProductAdapter.MyViewHolder holder, int position) {

        holder.tv_details_name.setText(productModels.get(position).getName());
        holder.tv_details_price.setText(String.valueOf(productModels.get(position).getPrice()));

        if (productModels.get(position).getImage() != null) {
            Glide.with(context)
                    .load(ImageUtils.convertStringImageToBitmap(productModels.get(position).getImage()))
                    .override(400, 400)
                    .into(holder.iv_details);
        } else {
            Glide.with(context)
                    .load(AppCompatResources.getDrawable(context, R.drawable.sale))
                    .override(400, 400)
                    .into(holder.iv_details);
        }

        holder.parentCard.setOnClickListener(v -> {
            replaceFragment(new DetailsFragment(),productModels.get(position).getId());
        });
    }

    private void replaceFragment(Fragment fragment, Long id) {
        Bundle args = new Bundle();
        args.putLong("productId",id);
        fragment.setArguments(args);
        FragmentTransaction fr = fragmentManager.beginTransaction();
        fr.replace(R.id.container, fragment);
        fr.addToBackStack(null);
        fr.commit();
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_details_name;
        private final TextView tv_details_price;
        private final ImageView iv_details;
        private final CardView parentCard;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_details_name = itemView.findViewById(R.id.tv_details_name);
            tv_details_price = itemView.findViewById(R.id.tv_details_price);
            iv_details = itemView.findViewById(R.id.iv_details);
            parentCard = itemView.findViewById(R.id.parent_card);
        }
    }
}


