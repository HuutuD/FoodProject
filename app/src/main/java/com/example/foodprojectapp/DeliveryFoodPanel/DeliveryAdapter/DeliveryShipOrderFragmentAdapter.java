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

import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryModels.DeliveryShipFinalOrders1;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryPendingOrderView;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryShipOrderView;
import com.example.foodprojectapp.DeliveryFoodPanel.Delivery_ShippingOrder;
import com.example.foodprojectapp.R;
import com.example.foodprojectapp.SendNotification.APIService;
import com.example.foodprojectapp.SendNotification.Client;
import com.example.foodprojectapp.SendNotification.Data;
import com.example.foodprojectapp.SendNotification.MyResponse;
import com.example.foodprojectapp.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryShipOrderFragmentAdapter extends RecyclerView.Adapter<DeliveryShipOrderFragmentAdapter.ViewHolder> {

    private Context context;
    private List<DeliveryShipFinalOrders1> deliveryShipFinalOrders1list;
    private APIService apiService;
    String deliveryId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";


    public DeliveryShipOrderFragmentAdapter(Context context, List<DeliveryShipFinalOrders1> deliveryShipFinalOrders1list) {
        this.deliveryShipFinalOrders1list = deliveryShipFinalOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_shiporders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new DeliveryShipOrderFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DeliveryShipFinalOrders1 deliveryShipFinalOrders1 = deliveryShipFinalOrders1list.get(position);
        holder.Address.setText(deliveryShipFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Grand Total: " + deliveryShipFinalOrders1.getGrandTotalPrice()+ " VND");
        holder.mobilenumber.setText("+84" + deliveryShipFinalOrders1.getMobileNumber());
        final String random = deliveryShipFinalOrders1.getRandomUID();
        final String userid = deliveryShipFinalOrders1.getUserId();

        holder.Vieworder.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeliveryPendingOrderView.class);
            intent.putExtra("Random", random);
            context.startActivity(intent);
        });

        holder.ShipOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Toast khi nhấp vào ShipOrder
                Toast.makeText(context, "Đơn hàng được nhận...", Toast.LENGTH_SHORT).show();

                // Không thực hiện thêm hành động nào khác
            }
        });


    }

    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryShipFinalOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice, mobilenumber;
        Button Vieworder, ShipOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Address = itemView.findViewById(R.id.ad2);
            mobilenumber = itemView.findViewById(R.id.MB2);
            grandtotalprice = itemView.findViewById(R.id.TP2);
            Vieworder = itemView.findViewById(R.id.view2);
            ShipOrder = itemView.findViewById(R.id.ship2);
        }
    }
}
