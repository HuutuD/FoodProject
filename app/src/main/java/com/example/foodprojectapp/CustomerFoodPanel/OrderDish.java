package com.example.foodprojectapp.CustomerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodprojectapp.ChefFoodPanel.ChefModels.Chef;
import com.example.foodprojectapp.ChefFoodPanel.UpdateDishModel;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin.CustomerFoodPanel_BottomNavigation;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerModels.Customer;

import com.example.foodprojectapp.CustomerFoodPanel.CustomerModels.Cart;
import com.example.foodprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDish extends AppCompatActivity {


    String RandomId, ChefID;
    Button btnIncrease, btnDecrease;
    TextView Foodname, ChefName, ChefLoaction, FoodQuantity, FoodPrice, FoodDescription, tvQuantity;
    DatabaseReference databaseReference, dataaa, chefdata, reference, data, dataref;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);

        Foodname = (TextView) findViewById(R.id.food_name);
        ChefName = (TextView) findViewById(R.id.chef_name);
        ChefLoaction = (TextView) findViewById(R.id.chef_location);
        FoodQuantity = (TextView) findViewById(R.id.food_quantity);
        FoodPrice = (TextView) findViewById(R.id.food_price);
        FoodDescription = (TextView) findViewById(R.id.food_description);
        btnIncrease = findViewById(R.id.btn_increase);
        btnDecrease = findViewById(R.id.btn_decrease);
        tvQuantity = findViewById(R.id.tv_quantity);

        tvQuantity.setText("1");

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    tvQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cust = dataSnapshot.getValue(Customer.class);
                State = cust.getState();
                City = cust.getCity();
                Sub = cust.getSuburban();

                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("ChefId");

                databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub).child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                        Foodname.setText(updateDishModel.getDishes());
                        String qua = "<b>" + "Quantity: " + "</b>" + updateDishModel.getQuantity();
                        FoodQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Description: " + "</b>" + updateDishModel.getDescription();
                        FoodDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Price: " + "</b>" + updateDishModel.getPrice() + " VND";
                        FoodPrice.setText(Html.fromHtml(pri));

                        chefdata = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Chef chef = dataSnapshot.getValue(Chef.class);

                                String name = "<b>" + "Chef Name: " + "</b>" + chef.getFname() + " " + chef.getLname();
                                ChefName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Location: " + "</b>" + chef.getSuburban();
                                ChefLoaction.setText(Html.fromHtml(loc));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);
                                        if (dataSnapshot.exists()) {
                                            tvQuantity.setText(cart.getDishQuantity());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                findViewById(R.id.btn_add_to_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(userId);

                        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart lastCartItem = null;
                                int totalItems = (int) dataSnapshot.getChildrenCount();
                                int itemCount = 0;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    itemCount++;
                                    if (itemCount == totalItems) {
                                        lastCartItem = snapshot.getValue(Cart.class);
                                    }
                                }

                                // Check if the chef ID matches the last item in the cart
                                if (lastCartItem != null && !ChefID.equals(lastCartItem.getChefId())) {
                                    showSingleChefAlert();
                                    return;
                                }

                                addDishToCart(userId);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addDishToCart(String userId) {
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                .child(State).child(City).child(Sub).child(ChefID).child(RandomId);

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);

                if (update != null) {
                    String dishName = update.getDishes();
                    int dishPrice = Integer.parseInt(update.getPrice());
                    int quantity = 1;
                    int totalPrice = quantity * dishPrice;

                    HashMap<String, String> cartItem = new HashMap<>();
                    cartItem.put("DishName", dishName);
                    cartItem.put("DishID", RandomId);
                    cartItem.put("DishQuantity", String.valueOf(quantity));
                    cartItem.put("Price", String.valueOf(dishPrice));
                    cartItem.put("Totalprice", String.valueOf(totalPrice));
                    cartItem.put("ChefId", ChefID);

                    DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference("Cart")
                            .child("CartItems").child(userId).child(RandomId);
                    userCartRef.setValue(cartItem).addOnSuccessListener(aVoid ->
                            Toast.makeText(OrderDish.this, "Added to cart", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void removeDishFromCart(String userId) {
        DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(userId).child(RandomId);
        cartItemRef.removeValue();
    }

    private void showSingleChefAlert() {
        new AlertDialog.Builder(OrderDish.this)
                .setMessage("You can't add food items of multiple chefs at a time. Try to add items from the same chef.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(OrderDish.this, CustomerFoodPanel_BottomNavigation.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }
}

