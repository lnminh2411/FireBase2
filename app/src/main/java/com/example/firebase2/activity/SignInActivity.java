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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private EditText txtUsername, txtPassword;
    private Button btnLogin;
    private TextView btnRegisterPage;
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-2-47a48-default-rtdb.asia-southeast1.firebasedatabase.app" );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterPage = findViewById(R.id.btnRegisterPage);
        btnRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String username = txtUsername.getText().toString();
//                String password = txtPassword.getText().toString();
//                if (username.isEmpty()||password.isEmpty()){
//                    Toast.makeText(SignInActivity.this, "Username or Password can not be blanked", Toast.LENGTH_SHORT).show();
//                }else{
//                    db.child("users").addChildEventListener(new ValueEventListener(){
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.hasChild("username")){
//                                String getPassword = dataSnapshot.child("password").getValue(String.class);
//                            }
//
//                            Toast.makeText(SignInActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                            // Failed to read value
//                            Toast.makeText(SignInActivit.this, "Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
    }
}