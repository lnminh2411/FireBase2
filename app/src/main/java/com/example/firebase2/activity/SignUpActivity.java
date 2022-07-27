package com.example.firebase2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.firebase2.R;
import com.example.firebase2.database.RestaurantDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText txtUsername, txtPassword, txtConfirmPassword;
    private Button btnRegister;
    private TextView btnLoginPage;
    private int currentId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginPage = findViewById(R.id.btnLoginPage);
        btnLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
        db.child(RestaurantDB.TABLE_ACCOUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();
                if ((!password.equals(confirmPassword))){
                    Toast.makeText(SignUpActivity.this, "Please check both having same password..", Toast.LENGTH_SHORT).show();
                }else{
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            db.child(RestaurantDB.TABLE_ACCOUNT).child("id").setValue(username);
                            db.child(RestaurantDB.TABLE_ACCOUNT).child("username").setValue(username);
                            db.child(RestaurantDB.TABLE_ACCOUNT).child("password").setValue(password);
                            Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}