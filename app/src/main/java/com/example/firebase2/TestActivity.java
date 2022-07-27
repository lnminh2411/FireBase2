package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase2.activity.ProductActivity;
import com.example.firebase2.adapter.ProductAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private Button pickButton, uploadButton;
    private TextView images;
    private RecyclerView recyclerView2;
    private static final int PICK_IMG = 1;
    private DatabaseReference databaseReference;
    private ArrayList<Uri> uri = new ArrayList<>();
    private int uploads = 0;
    private TestAdapter adapter;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        pickButton = findViewById(R.id.pickButton);
        uploadButton = findViewById(R.id.uploadButton);
        images = findViewById(R.id.images);
        recyclerView2 = findViewById(R.id.recyclerView2);

        adapter = new TestAdapter(uri);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 5, LinearLayoutManager.HORIZONTAL,false ));
        recyclerView2.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Multiple Images");

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFiles(view);

            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFiles(view);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode == PICK_IMG) {
//            if (resultCode == RESULT_OK) {
//                if (data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//
//                    int CurrentImageSelect = 0;
//
//                    while (CurrentImageSelect < count) {
//                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
//                        uri.add(imageuri);
//                        CurrentImageSelect = CurrentImageSelect + 1;
//                    }
//                    images.setVisibility(View.VISIBLE);
//                    images.setText("You Have Selected "+ uri.size() +" Pictures" );
//                    pickButton.setVisibility(View.GONE);
//                }
//            }
//        }
        if (requestCode == PICK_IMG && resultCode == Activity.RESULT_OK){
            if (data.getClipData() != null){
                int x = data.getClipData().getItemCount();
                for (int i=0; i<x;i++){
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                images.setText("You Have Selected "+ uri.size() +" Pictures" );
                pickButton.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }else{
                Uri imageUri = data.getData();
                uri.add(imageUri);
            }
            images.setText("You Have Selected "+ uri.size() +" Pictures" );
            pickButton.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "You haven't picked any images!!!",Toast.LENGTH_LONG).show();
        }
    }
    public void chooseFiles(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }
    public void uploadFiles(View view){
        images.setText("Please Wait ... If Uploading takes Too much time please the button again ");
        final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child("ImageFolder");
        for (uploads=0; uploads < uri.size(); uploads++) {
            Uri Image  = uri.get(uploads);
            final StorageReference imagename = ImageFolder.child("image/"+System.currentTimeMillis());

            imagename.putFile(uri.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);
                            SendLink(url);
                        }
                    });

                }
            });
        }

    }
    private void SendLink(String url) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("link", url);
        databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                images.setText("Image Uploaded Successfully");
                uploadButton.setVisibility(View.GONE);
                uri.clear();
            }
        });
    }
}