package com.example.foodprojectapp.SendNotification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.foodprojectapp.ChefFoodPanel.ChefPreparedOrderView;
import com.example.foodprojectapp.ChefFoodPanel.ChefLogin.ChefFoodPanel_BottomNavigation;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin.CustomerFoodPanel_BottomNavigation;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryLogin.Delivery_FoodPanelBottomNavigation;
import com.example.foodprojectapp.MainActivity;
import com.example.foodprojectapp.R;

import java.util.Random;

public class ShowNotification {

    @SuppressLint("MissingPermission")
    public static void ShowNotif(Context context, String title, String message, String page) {
        String CHANNEL_ID = "NOTICE";
        String CHANNEL_NAME = "NOTICE";

        // Tạo NotificationChannel cho các phiên bản Android O trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent acIntent = new Intent(context, MainActivity.class);
        acIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Thêm cờ mutability cho PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

        if (page.trim().equalsIgnoreCase("Order")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "Orderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("Home")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Homepage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("Confirm")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "Confirmpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("Preparing")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Preparingpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("Prepared")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Preparedpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("DeliveryOrder")) {
            acIntent = new Intent(context, Delivery_FoodPanelBottomNavigation.class).putExtra("PAGE", "DeliveryOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("DeliverOrder")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "DeliverOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("AcceptOrder")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "AcceptOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("RejectOrder")) {
            acIntent = new Intent(context, ChefPreparedOrderView.class).putExtra("PAGE", "RejectOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("ThankYou")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "ThankYoupage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }
        if (page.trim().equalsIgnoreCase("Delivered")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "Deliveredpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chef_hat_and_fork)
                .setColor(ContextCompat.getColor(context, R.color.Red))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        int random = new Random().nextInt(9999 - 1) + 1;
        notificationManagerCompat.notify(random, nBuilder.build());
    }
}
