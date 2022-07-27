package com.example.firebase2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase2.R;
import com.example.firebase2.TestAdapter;
import com.example.firebase2.adapter.ProductAdapter;
import com.example.firebase2.database.RestaurantDB;
import com.example.firebase2.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    ProductAdapter adapter;
    private TextView totalImages;
    int PICK_IMAGE_MULTIPLE = 1;
    int maxId = 0;
    private int productId = 0;
    private EditText txtFoodName;
    private Spinner txtCategoryList;
    private EditText numPrice;
    private Button btnAddFood;
    private Button btnPick;
    private List<String> categoryList = new ArrayList<>();
    private ArrayList<Uri> imgList = new ArrayList<>();
    private RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference("Food");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        txtFoodName = findViewById(R.id.txtFoodName);
        txtCategoryList = findViewById(R.id.txtCategoryList);
        numPrice = findViewById(R.id.numPrice);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnPick = findViewById(R.id.btnPick);
        recyclerView = findViewById(R.id.recyclerView);
        totalImages = findViewById(R.id.totalImages);

        adapter = new ProductAdapter(imgList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFiles(view);
            }
        });

        database.getReference("Category").child(RestaurantDB.TABLE_CATEGORY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String items = childSnapshot.child("categoryName").getValue(String.class);
                    categoryList.add(items);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductActivity.this, android.R.layout.simple_spinner_item, categoryList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                txtCategoryList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query = db.child(RestaurantDB.TABLE_PRODUCT).orderByKey().limitToLast(1);
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
                Toast.makeText(ProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFireBase(view);
            }
        });
    }

    public void chooseFiles(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    imgList.add(data.getClipData().getItemAt(i).getUri());
                }
                totalImages.setText("You Have Selected " + imgList.size() + " Pictures");
                adapter.notifyDataSetChanged();
            } else {
                Uri imageUri = data.getData();
                imgList.add(imageUri);
            }
            totalImages.setText("You Have Selected " + imgList.size() + " Pictures");
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "You haven't picked any images!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadToFireBase(View view) {
//        StorageReference storageReference = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        //                    startActivity(new Intent(ProductActivity.this, FoodActivity.class));
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                Toast.makeText(ProductActivity.this, "Uploading in progress!!!", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ProductActivity.this, "Uploading failed!!!", Toast.LENGTH_SHORT).show();
//            }
//        });
        productId = maxId + 1;
        String productName = txtFoodName.getText().toString();
        float price = Float.valueOf(numPrice.getText().toString());
        String category = txtCategoryList.getSelectedItem().toString();
        ArrayList<String> list = new ArrayList<>();
        totalImages.setText("Please Wait ... If Uploading takes Too much time please the button again ");
        final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        for (int uploads = 0; uploads < imgList.size(); uploads++) {
            Uri Image = imgList.get(uploads);
            final StorageReference imagename = ImageFolder.child(productId + "/" + System.currentTimeMillis()+"."+getFileExtension(Image));
            imagename.putFile(imgList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgList.add(uri);
                            list.add(String.valueOf(uri));

                            db.child(RestaurantDB.TABLE_PRODUCT).child(String.valueOf(productId)).child("Food Name").setValue(productName);
                            db.child(RestaurantDB.TABLE_PRODUCT).child(String.valueOf(productId)).child("Category").setValue(category);
                            db.child(RestaurantDB.TABLE_PRODUCT).child(String.valueOf(productId)).child("Price").setValue(String.valueOf(price));
                            db.child(RestaurantDB.TABLE_PRODUCT).child(String.valueOf(productId)).child("Image").setValue(list);
                            Toast.makeText(ProductActivity.this, "Upload Successfully!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

//    private void SendLink(String url) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("link", url);
//        db.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                totalImages.setText("Image Uploaded Successfully");
//                imgList.clear();
//            }
//        });
//    }
}