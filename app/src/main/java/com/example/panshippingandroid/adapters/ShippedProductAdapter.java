package com.example.panshippingandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.time.LocalDateTime;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ShippedProductAdapter extends RecyclerView.Adapter<ShippedProductAdapter.MyViewHolder> {

    private final Context context;
    private List<ProductDto> productModels;
    private FragmentManager fragmentManager;
    public Long userId;
    private LocalDateTime nowDate = LocalDateTime.now();
    private String startTime;
    private String endTime;

    public ShippedProductAdapter(Context context, FragmentManager fragmentManager, List<ProductDto> productDtoList) {
        this.context = context;
        this.productModels = productDtoList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ShippedProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_single_item_buy, parent, false);
        return new ShippedProductAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShippedProductAdapter.MyViewHolder holder, int position) {

        holder.byNameTv.setText(productModels.get(position).getName());
        holder.byPriceTv.setText((productModels.get(position).getPrice()) + " €");

        startTime = String.valueOf(productModels.get(position).getShipping().get(position).getStart());
        endTime = String.valueOf(productModels.get(position).getShipping().get(position).getEnd());

        LocalDateTime startDate = LocalDateTime.parse(startTime);
        LocalDateTime endDate = LocalDateTime.parse(endTime);

        if (startDate.isAfter(nowDate)) {
            holder.byStatus.setText(R.string.progress);
        } else if (endDate.isAfter(nowDate)) {
            holder.byStatus.setText(R.string.transit);
        } else {
            holder.byStatus.setText(R.string.delivered);
        }

        if (productModels.get(position).getImage() != null) {
            Glide.with(context)
                    .load(ImageUtils.convertStringImageToBitmap(productModels.get(position).getImage()))
                    .override(400, 400)
                    .into(holder.byImageIv);
        } else {
            Glide.with(context)
                    .load(AppCompatResources.getDrawable(context, R.drawable.sale))
                    .override(400, 400)
                    .into(holder.byImageIv);
        }

        holder.parentCard.setOnClickListener(v -> {
            replaceFragment(new DetailsFragment(), productModels.get(position).getId());
        });
    }

    private void replaceFragment(Fragment fragment, Long id) {
        Bundle args = new Bundle();
        args.putLong("productId", id);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView byNameTv;
        private final TextView byPriceTv;
        private final ImageView byImageIv;
        private final CardView parentCard;
        private final TextView byStatus;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            byNameTv = itemView.findViewById(R.id.byNameTv);
            byPriceTv = itemView.findViewById(R.id.byPriceTv);
            byImageIv = itemView.findViewById(R.id.byImageIv);
            parentCard = itemView.findViewById(R.id.parent_card);
            byStatus = itemView.findViewById(R.id.byStatus);

        }
    }
}


