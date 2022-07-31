package com.example.firebase2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase2.R;
import com.example.firebase2.adapter.ProductAdapter;
import com.example.firebase2.database.RestaurantDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    ProductAdapter adapter;
    String category = "";
    private TextView totalImages;
    int PICK_IMAGE_MULTIPLE = 1;
    int maxId = 0;
    private int productId = 0;
    private EditText txtFoodName, txtDescription;
    private Spinner txtCategoryList;
    private EditText numPrice;
    private Button btnAddFood;
    private Button btnPick;
    private List<String> categoryList = new ArrayList<>();
    private ArrayList<Uri> imgList = new ArrayList<>();
    private RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference("Food");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        txtFoodName = findViewById(R.id.txtFoodName);
        txtCategoryList = findViewById(R.id.txtCategoryList);
        txtDescription = findViewById(R.id.txtDescription);
        numPrice = findViewById(R.id.numPrice);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnPick = findViewById(R.id.btnPick);
        recyclerView = findViewById(R.id.food_recycler);
        totalImages = findViewById(R.id.totalImages);
        //("Chọn thể loại","Cơm","Lẩu","Phở","Mì","Súp","Đồ Uống","Tráng miệng","Ăn nhanh","Combo");
        categoryList.add(0, "Chọn thể loại");
        categoryList.add("Cơm");
        categoryList.add("Lẩu");
        categoryList.add("Phở");
        categoryList.add("Mì");
        categoryList.add("Súp");
        categoryList.add("Đồ Uống");
        categoryList.add("Tráng miệng");
        categoryList.add("Ăn nhanh");
        categoryList.add("Combo");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductActivity.this, android.R.layout.simple_spinner_item, categoryList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        txtCategoryList.setAdapter(arrayAdapter);

        adapter = new ProductAdapter(imgList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        txtCategoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Chọn thể loại")) {
                    category = null;
                } else {
                    String item = adapterView.getItemAtPosition(i).toString();
                    category = item;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFiles(view);
            }
        });

        Query query = db.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    try{
                        maxId = Integer.parseInt(childSnapshot.getKey());
                    } catch(NumberFormatException ex){ // handle your exception
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            totalImages.setText("You haven't picked any images!!!");
        }
    }

    private void uploadToFireBase(View view) {
        productId = maxId + 1;
        ArrayList<String> list = new ArrayList<>();
        if (txtFoodName.getText().toString().isEmpty() || txtDescription.getText().toString().isEmpty() || numPrice.getText().toString().isEmpty()){
            Toast.makeText(ProductActivity.this, "Can not blanked", Toast.LENGTH_SHORT).show();
            ProductActivity.this.recreate();
        } else if (category == null) {
            Toast.makeText(ProductActivity.this, "Mời chọn thể loại", Toast.LENGTH_SHORT).show();
            ProductActivity.this.recreate();
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String productName = txtFoodName.getText().toString();
            String description = txtDescription.getText().toString();
            float price = Float.valueOf(numPrice.getText().toString());
            final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
            for (int uploads = 0; uploads < imgList.size(); uploads++) {
                Uri Image = imgList.get(uploads);
                final StorageReference imagename = ImageFolder.child(productId + "/" + System.currentTimeMillis() + "." + getFileExtension(Image));
                imagename.putFile(imgList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imgList.add(uri);
                                list.add(String.valueOf(uri));
                                list.size();
                                db.child(String.valueOf(productId)).child("Food Name").setValue(productName);
                                db.child(String.valueOf(productId)).child("Category").setValue(category);
                                db.child(String.valueOf(productId)).child("Price").setValue(String.valueOf(price));
                                db.child(String.valueOf(productId)).child("Description").setValue(description);
                                db.child(String.valueOf(productId)).child("Image").setValue(list);
                                db.child(String.valueOf(productId)).child("Date Add").setValue(now.toString());
                                Toast.makeText(ProductActivity.this, "Upload Successfully!!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
            }
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