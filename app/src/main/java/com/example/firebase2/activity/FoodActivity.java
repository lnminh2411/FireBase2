package com.example.firebase2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.firebase2.R;

import com.example.firebase2.adapter.FoodAdapter;
import com.example.firebase2.adapter.ProductAdapter;
import com.example.firebase2.database.RestaurantDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity{
    String[] imageList={};
    int count=imageList.length;
    int currentIndex=0;
    int productId = 1;
    FoodAdapter adapter;
    RecyclerView foodRecycler;
    TextView foodName, foodPrice, foodDescription;
    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<Uri> uriList = new ArrayList<>();
    ImageView imageView;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("Food");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        foodRecycler = findViewById(R.id.foodRecycler);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDescription = findViewById(R.id.foodDescription);

        adapter = new FoodAdapter(imgList,this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        foodRecycler.setLayoutManager(layoutManager);

        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("ImageFolder/").child(String.valueOf(productId));
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference file:listResult.getItems()){
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgList.add(uri.toString());
                            Log.e("Itemvalue",uri.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            foodRecycler.setAdapter(adapter);
                        }
                    });
                }
            }
        });
        db.child(String.valueOf(productId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodName.setText(snapshot.child("Food Name").getValue(String.class));
                foodPrice.setText(snapshot.child("Price").getValue(String.class));
                foodDescription.setText(snapshot.child("Description").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}