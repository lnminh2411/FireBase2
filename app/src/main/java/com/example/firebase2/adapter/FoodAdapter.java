//package com.example.firebase2.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.firebase2.R;
//import com.example.firebase2.model.Product;
//
//import java.util.ArrayList;
//
//public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {
//    private Product[] productArray;
//
//    public FoodAdapter(Product[] productArray){
//        this.productArray = productArray;
//
//    }
//
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_items,parent,false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.imgViewIcon.setImageResource(Integer.parseInt(productArray[position].getImage()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return productArray.length;
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        public ImageView imgViewIcon;
//        public MyViewHolder (@NonNull View itemView){
//            super(itemView);
//            imgViewIcon = itemView.findViewById(R.id.imgViewIcon);
//        }
//    }
//
//
//}
