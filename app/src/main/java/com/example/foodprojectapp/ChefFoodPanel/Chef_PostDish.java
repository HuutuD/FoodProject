package com.example.foodprojectapp.ChefFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodprojectapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class Chef_PostDish extends AppCompatActivity {

    ImageButton imageButton;
    Button post_dish;
    Spinner Dishes;
    TextInputLayout desc, qty, pri;
    String description, quantity, price, dishes;
    Uri imageuri; // Không cần thiết nếu không sử dụng hình ảnh
    DatabaseReference databaseReference;
    FirebaseAuth FAuth;
    String ChefId;
    String RandomUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef__post_dish);

        Dishes = findViewById(R.id.dishes);
        desc = findViewById(R.id.description);
        qty = findViewById(R.id.quantity);
        pri = findViewById(R.id.price);
        post_dish = findViewById(R.id.post);
        FAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (FAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please log in to post a dish", Toast.LENGTH_SHORT).show();
            return;
        }

        String userid = FAuth.getCurrentUser().getUid();
        DatabaseReference chefRef = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
        chefRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ChefId = dataSnapshot.child("ChefId").getValue(String.class);
                } else {
                    Toast.makeText(Chef_PostDish.this, "Chef data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chef_PostDish.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });

        imageButton = findViewById(R.id.imageupload);
        imageButton.setVisibility(View.GONE); // Ẩn nút tải lên hình ảnh nếu không cần

        post_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDish();
            }
        });
    }

    private void PostDish() {
        description = desc.getEditText().getText().toString();
        quantity = qty.getEditText().getText().toString();
        price = pri.getEditText().getText().toString();
        dishes = Dishes.getSelectedItem().toString();

        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RandomUId = UUID.randomUUID().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting Dish...");
        progressDialog.show();

        // Lưu dữ liệu vào Realtime Database
        HashMap<String, Object> dishData = new HashMap<>();
        dishData.put("DishId", RandomUId);
        dishData.put("ChefId", ChefId);
        dishData.put("Description", description);
        dishData.put("Quantity", quantity);
        dishData.put("Price", price);
        dishData.put("DishName", dishes);
            dishData.put("ImageUrl", "static_image_url_here"); // Thay bằng URL hình ảnh tĩnh nếu có

        databaseReference.child(RandomUId).setValue(dishData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(Chef_PostDish.this, "Dish posted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Chef_PostDish.this, "Failed to post dish", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

