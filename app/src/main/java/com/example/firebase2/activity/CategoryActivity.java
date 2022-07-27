package com.example.firebase2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase2.R;
import com.example.firebase2.database.RestaurantDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class CategoryActivity extends AppCompatActivity {
    private EditText txtCategoryName;
    private Button btnAddCategory;
    int maxId = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference("Category");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        txtCategoryName = findViewById(R.id.txtCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        Query query = db.child(RestaurantDB.TABLE_CATEGORY).orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    System.out.println(childSnapshot.getKey());
                    maxId = Integer.parseInt(childSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int categoryId = maxId+1;
                String categoryName = txtCategoryName.getText().toString();
                db.child(RestaurantDB.TABLE_CATEGORY).child(String.valueOf(categoryId)).child("categoryName").setValue(categoryName);
                Toast.makeText(CategoryActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
}