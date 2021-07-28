package com.example.panshippingandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.model.ProductDto;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private final Context context;
    private final List<ProductDto> productModels;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public RVAdapter(Context context, List<ProductDto> productDtoList) {
        this.context = context;
        this.productModels = productDtoList;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_single_item, parent, false);

        return new RVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final TextView tv_price;
        private final ImageView imageView;
        private TextView tv_quantity;
        private TextView tv_description;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.rv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            imageView = itemView.findViewById(R.id.rv_image);

        }
    }
}



