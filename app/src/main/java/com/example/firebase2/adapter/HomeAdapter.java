package com.example.firebase2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase2.R;
import com.example.firebase2.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
    private List<Product> productList;
    private Context context;

    public HomeAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.food_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(productList.get(position)).into(holder.mImage);
        holder.mFoodName.setText(this.productList.get(position).getName());
        holder.mFoodPrice.setText(String.valueOf(this.productList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImage;
        public TextView mFoodName;
        public TextView mFoodPrice;
        public MyViewHolder (@NonNull View itemView){
            super(itemView);
            mImage = itemView.findViewById(R.id.mImage);
            mFoodName = itemView.findViewById(R.id.mFoodName);
            mFoodPrice = itemView.findViewById(R.id.mFoodPrice);
        }
    }
}
