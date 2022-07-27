package com.example.firebase2;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase2.adapter.ProductAdapter;

import java.util.ArrayList;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder>{
    private ArrayList<Uri> uriArrayList;

    public TestAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.image_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imgViewIcon.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgViewIcon;
        public MyViewHolder (@NonNull View itemView){
            super(itemView);
            imgViewIcon = itemView.findViewById(R.id.imgViewIcon);
        }
    }
}
