package com.example.foodprojectapp.ChefFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodprojectapp.ChefFoodPanel.ChefModels.Chef;
import com.example.foodprojectapp.ChefFoodPanel.ChefModels.FoodSupplyDetails;
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

    Button post_dish;
    Spinner Dishes;
    TextInputLayout desc, qty, pri;
    String description, quantity, price, dishes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataaa;
    FirebaseAuth FAuth;
    String ChefId;
    String RandomUId;
    String State, City, Sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef__post_dish);

        Dishes = (Spinner) findViewById(R.id.dishes);
        desc = (TextInputLayout) findViewById(R.id.description);
        qty = (TextInputLayout) findViewById(R.id.quantity);
        pri = (TextInputLayout) findViewById(R.id.price);
        post_dish = (Button) findViewById(R.id.post);
        FAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("FoodSupplyDetails");

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Chef chefc = dataSnapshot.getValue(Chef.class);
                    State = chefc.getState();
                    City = chefc.getCity();
                    Sub = chefc.getSuburban();

                    post_dish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dishes = Dishes.getSelectedItem().toString().trim();
                            description = desc.getEditText().getText().toString().trim();
                            quantity = qty.getEditText().getText().toString().trim();
                            price = pri.getEditText().getText().toString().trim();

                            if (isValid()) {
                                postDish();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValidDescription = false, isValidPrice = false, isValidQuantity = false, isValid = false;
        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        } else {
            desc.setError(null);
            isValidDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            qty.setErrorEnabled(true);
            qty.setError("Quantity is Required");
        } else {
            isValidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Price is Required");
        } else {
            isValidPrice = true;
        }
        isValid = isValidDescription && isValidQuantity && isValidPrice;

        return isValid;
    }

    private void postDish() {
        RandomUId = UUID.randomUUID().toString();
        ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FoodSupplyDetails info = new FoodSupplyDetails(dishes, quantity, price, description, RandomUId, ChefId);

        // Lưu dữ liệu theo địa chỉ của bếp trưởng
        firebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                .child(State).child(City).child(Sub)
                .child(ChefId).child(RandomUId)
                .setValue(info)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Chef_PostDish.this, "Dish posted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Chef_PostDish.this, "Failed to post dish", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

