package com.example.firebase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.firebase2.activity.HomeActivity;
import com.example.firebase2.database.RestaurantDB;
import com.example.firebase2.model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_rice, btn_hot_pot, btn_noodle_soup, btn_soup, btn_dessert, btn_fast_food, btn_combo, btn_drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_rice = findViewById(R.id.button_rice);
        btn_hot_pot = findViewById(R.id.button_hot_pot);
        btn_noodle_soup = findViewById(R.id.button_noodle_soup);
        btn_soup = findViewById(R.id.button_soup);
        btn_dessert = findViewById(R.id.button_dessert);
        btn_fast_food = findViewById(R.id.button_fast_food);
        btn_combo = findViewById(R.id.button_combo);
        btn_drink = findViewById(R.id.button_drink);

        btn_rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Cơm";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_hot_pot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Lẩu";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_noodle_soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Mì";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Súp";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Tráng miệng";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_fast_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Ăn nhanh";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Combo";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
        btn_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                String category = "Đò uống";
                intent.putExtra("categoryName", category);
                startActivity(intent);
            }
        });
    }

}