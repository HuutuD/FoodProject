package com.example.foodprojectapp.DeliveryFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryAdapter.DeliveryPendingOrderViewAdapter;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryModels.DeliveryShipOrders;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryModels.DeliveryShipOrders1;
import com.example.foodprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPendingOrderView extends AppCompatActivity {

    private RecyclerView recyclerViewdish;
    private List<DeliveryShipOrders> deliveryShipOrdersList;
    private DeliveryPendingOrderViewAdapter adapter;
    private DatabaseReference reference;
    private String randomUID;
    private TextView grandtotal, address, name, number, chefName;
    private LinearLayout l1;
    String deliveryId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_pending_order_view);

        // Khởi tạo các View
        recyclerViewdish = findViewById(R.id.delivieworder);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(this));

        l1 = findViewById(R.id.linear1);
        grandtotal = findViewById(R.id.Dtotal);
        address = findViewById(R.id.DAddress);
        chefName = findViewById(R.id.chefname);
        name = findViewById(R.id.DName);
        number = findViewById(R.id.DNumber);

        deliveryShipOrdersList = new ArrayList<>();
        deliveryorders();
    }

    private void deliveryorders() {
        randomUID = getIntent().getStringExtra("Random");

        if (randomUID == null) {
            Toast.makeText(this, "Mã đơn hàng không tìm thấy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Truy cập danh sách các món ăn
        reference = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders")
                .child(deliveryId)
                .child(randomUID)
                .child("Dishes");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryShipOrdersList.clear(); // Xóa danh sách trước khi cập nhật
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryShipOrders deliveryShipOrders = snapshot.getValue(DeliveryShipOrders.class);
                    if (deliveryShipOrders != null) {
                        deliveryShipOrdersList.add(deliveryShipOrders);
                    }
                }

                // Cập nhật hiển thị của LinearLayout
                l1.setVisibility(deliveryShipOrdersList.isEmpty() ? View.INVISIBLE : View.VISIBLE);
                adapter = new DeliveryPendingOrderViewAdapter(DeliveryPendingOrderView.this, deliveryShipOrdersList);
                recyclerViewdish.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DeliveryPendingOrderView.this, "Không thể tải đơn hàng: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference otherInfoReference = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders")
                .child(deliveryId)
                .child(randomUID)
                .child("OtherInformation");

        otherInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot;
                DeliveryShipOrders1 deliveryShipOrders1 = dataSnapshot.getValue(DeliveryShipOrders1.class);
                if (deliveryShipOrders1 != null) {
                    grandtotal.setText(deliveryShipOrders1.getGrandTotalPrice() + "  VND");
                    address.setText(deliveryShipOrders1.getAddress());
                    name.setText(deliveryShipOrders1.getName());
                    number.setText("+84" + deliveryShipOrders1.getMobileNumber());
                    chefName.setText("Chef " + deliveryShipOrders1.getChefName());
                } else {
                    Toast.makeText(DeliveryPendingOrderView.this, "Thông tin đơn hàng không tìm thấy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DeliveryPendingOrderView.this, "Không thể tải thông tin đơn hàng: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
