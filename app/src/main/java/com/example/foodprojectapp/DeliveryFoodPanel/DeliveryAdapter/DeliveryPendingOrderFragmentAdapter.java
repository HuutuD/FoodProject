package com.example.foodprojectapp.DeliveryFoodPanel.DeliveryAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryPendingOrderView;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryModels.DeliveryShipOrders;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryModels.DeliveryShipOrders1;
import com.example.foodprojectapp.R;
import com.example.foodprojectapp.SendNotification.APIService;
import com.example.foodprojectapp.SendNotification.Client;
import com.example.foodprojectapp.SendNotification.Data;
import com.example.foodprojectapp.SendNotification.MyResponse;
import com.example.foodprojectapp.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryPendingOrderFragmentAdapter extends RecyclerView.Adapter<DeliveryPendingOrderFragmentAdapter.ViewHolder> {

    private final Context context;
    private final List<DeliveryShipOrders1> deliveryShipOrders1list;
    private final APIService apiService;
    String deliveryId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";

    public DeliveryPendingOrderFragmentAdapter(Context context, List<DeliveryShipOrders1> deliveryShipOrders1list) {
        this.context = context;
        this.deliveryShipOrders1list = deliveryShipOrders1list;
        this.apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_pendingorders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeliveryShipOrders1 order = deliveryShipOrders1list.get(position);

        holder.Address.setText(order.getAddress());
        holder.mobilenumber.setText("+84" + order.getMobileNumber());
        holder.grandtotalprice.setText("Grand Total: VND " + order.getGrandTotalPrice());

        final String randomuid = order.getRandomUID();

        holder.Vieworder.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeliveryPendingOrderView.class);
            intent.putExtra("Random", randomuid);
            context.startActivity(intent);
        });

        holder.Accept.setOnClickListener(v -> handleAcceptOrder(randomuid));
        holder.Reject.setOnClickListener(v -> handleRejectOrder(randomuid));
    }

    private void handleAcceptOrder(String randomuid) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference("DeliveryShipOrders")  // Thêm nhánh chính
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(randomuid)
                .child("Dishes");

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryShipOrders deliveryOrder = snapshot.getValue(DeliveryShipOrders.class);
                    if (deliveryOrder != null) {
                        addFinalOrder(deliveryOrder, randomuid);
                    }
                }
                clearPendingOrder(randomuid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to accept order: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRejectOrder(String randomuid) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference("DeliveryShipOrders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(randomuid)
                .child("Dishes");

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String chefId = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryShipOrders deliveryOrder = snapshot.getValue(DeliveryShipOrders.class);
                    if (deliveryOrder != null) {
                        chefId = deliveryOrder.getChefId();
                        break;
                    }
                }
                if (chefId != null) {
                    sendNotification(chefId, "Order Rejected", "Your Order has been Rejected by the Delivery person", "RejectOrder");
                }
                clearPendingOrder(randomuid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to reject order: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addFinalOrder(DeliveryShipOrders order, String randomuid) {
        HashMap<String, String> orderDetails = new HashMap<>();
        orderDetails.put("ChefId", order.getChefId());
        orderDetails.put("DishId", order.getDishId());
        orderDetails.put("DishName", order.getDishName());
        orderDetails.put("DishPrice", order.getDishPrice());
        orderDetails.put("DishQuantity", order.getDishQuantity());
        orderDetails.put("RandomUID", randomuid); // Lưu RandomUID trong thông tin đơn hàng
        orderDetails.put("TotalPrice", order.getTotalPrice());
        orderDetails.put("UserId", order.getUserId());

        FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(randomuid)
                .child("Dishes")
                .child(order.getDishId())
                .setValue(orderDetails);
    }

    private void clearPendingOrder(String randomuid) {
        DatabaseReference pendingRef = FirebaseDatabase.getInstance()
                .getReference("DeliveryShipOrders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(randomuid);

        pendingRef.child("Dishes").removeValue();
        pendingRef.child("OtherInformation").removeValue();
    }

    private void sendNotification(String chefId, String title, String message, String orderStatus) {
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("Tokens").child(chefId).child("token");

        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.getValue(String.class);
                if (token != null) {
                    NotificationSender sender = new NotificationSender(new Data(title, message, orderStatus), token);
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() != 200 || response.body().success != 1) {
                                Toast.makeText(context, "Failed to send notification", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(context, "Notification error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to retrieve token: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryShipOrders1list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Address, grandtotalprice, mobilenumber;
        Button Vieworder, Accept, Reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Address = itemView.findViewById(R.id.ad1);
            mobilenumber = itemView.findViewById(R.id.MB1);
            grandtotalprice = itemView.findViewById(R.id.TP1);
            Vieworder = itemView.findViewById(R.id.view1);
            Accept = itemView.findViewById(R.id.accept1);
            Reject = itemView.findViewById(R.id.reject1);
        }
    }
}
