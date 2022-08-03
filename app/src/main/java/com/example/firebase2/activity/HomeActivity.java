package com.example.firebase2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.firebase2.R;
import com.example.firebase2.adapter.FoodAdapter;
import com.example.firebase2.adapter.HomeAdapter;
import com.example.firebase2.database.RestaurantDB;
import com.example.firebase2.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Product getProduct;
    String categoryName = "";
    private List<Product> productList = new ArrayList<>();
    HomeAdapter adapter;
    int x = 0;
    int productId;
    ArrayList<String> imgList = new ArrayList<>();
    RecyclerView food_recycler;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference("Food");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        food_recycler = findViewById(R.id.food_recycler);

        Intent data = getIntent();
        categoryName = data.getStringExtra("categoryName");

        adapter = new HomeAdapter(productList, getApplicationContext());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        food_recycler.setLayoutManager(layoutManager);
        db.orderByChild("Category").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    productId = Integer.parseInt(childSnapshot.getKey());
                    StorageReference listRef = FirebaseStorage.getInstance().getReference().child("ImageFolder").child(String.valueOf(productId));
                    listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            for (StorageReference file : listResult.getItems()) {
                                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imgList.add(uri.toString());
                                        Log.e("Itemvalue", uri.toString());
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        food_recycler.setAdapter(adapter);

                                    }
                                });
                            }
                        }
                    });
                    db.child(String.valueOf(productId)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            getProduct = new Product(snapshot.child("Food Name").getValue(String.class), Float.valueOf(snapshot.child("Price").getValue(String.class)));
                            productList.add(getProduct);

                            food_recycler.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


//                StorageReference listRef = FirebaseStorage.getInstance().getReference().child("ImageFolder").child(String.valueOf(productId));
//                listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        for (StorageReference file : listResult.getItems()) {
//                            file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    imgList.add(uri.toString());
//                                    Log.e("Itemvalue", uri.toString());
//                                }
//                            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//
//
//                                }
//                            });
//                        }
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
