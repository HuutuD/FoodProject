package com.example.foodprojectapp.DeliveryFoodPanel.DeliveryLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foodprojectapp.R;
import com.example.foodprojectapp.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Delivery_ForgotPassword extends AppCompatActivity {

    TextInputLayout forgetpassword;
    Button Reset;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__forgot_password);

        forgetpassword = findViewById(R.id.forgotEmailid);
        Reset = findViewById(R.id.forgotreset);

        FAuth = FirebaseAuth.getInstance();
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo AlertDialog với ProgressBar
                AlertDialog.Builder builder = new AlertDialog.Builder(Delivery_ForgotPassword.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_progress, null); // Tạo layout cho dialog progress
                ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
                TextView messageTextView = dialogView.findViewById(R.id.progressMessage);
                messageTextView.setText("Logging in..."); // Thiết lập thông điệp

                builder.setView(dialogView);
                builder.setCancelable(false);  // Không cho phép hủy khi chạm bên ngoài

                AlertDialog mDialog = builder.create();
                mDialog.show(); // Hiển thị dialog


                FAuth.sendPasswordResetEmail(forgetpassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(Delivery_ForgotPassword.this, "", "Password has been sent to your Email");
                            Intent intent = new Intent(Delivery_ForgotPassword.this, Delivery_Login.class);
                            startActivity(intent);
                        } else {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(Delivery_ForgotPassword.this, "Error", task.getException().getMessage());
                        }
                    }
                });
            }
        });

    }
}
