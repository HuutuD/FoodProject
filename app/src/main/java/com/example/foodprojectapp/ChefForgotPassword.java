package com.example.foodprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.foodprojectapp.R;
import com.example.foodprojectapp.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ChefForgotPassword extends AppCompatActivity {

    TextInputLayout forgetpassword;
    Button Reset;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_forgot_password);

        forgetpassword = (TextInputLayout) findViewById(R.id.Emailid);
        Reset = (Button) findViewById(R.id.button2);

        FAuth = FirebaseAuth.getInstance();
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay thế ProgressDialog bằng AlertDialog với ProgressBar tùy chỉnh
                AlertDialog.Builder builder = new AlertDialog.Builder(ChefForgotPassword.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_progress, null);
                ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

                builder.setView(dialogView);
                builder.setCancelable(false);  // không cho phép hủy khi chạm bên ngoài

                AlertDialog mDialog = builder.create();
                mDialog.show();

                // Gửi yêu cầu đặt lại mật khẩu
                FAuth.sendPasswordResetEmail(forgetpassword.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mDialog.dismiss();  // Tắt dialog khi hoàn thành
                                if (task.isSuccessful()) {
                                    ReusableCodeForAll.ShowAlert(ChefForgotPassword.this, "", "Password has been sent to your Email");
                                } else {
                                    ReusableCodeForAll.ShowAlert(ChefForgotPassword.this, "Error", task.getException().getMessage());
                                }
                            }
                        });
            }
        });

    }
}
