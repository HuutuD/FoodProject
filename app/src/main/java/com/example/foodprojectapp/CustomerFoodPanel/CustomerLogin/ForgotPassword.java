package com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodprojectapp.R;
import com.example.foodprojectapp.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    TextInputLayout forgetpassword;
    Button Reset;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgetpassword = findViewById(R.id.Emailid);
        Reset = findViewById(R.id.button2);

        FAuth = FirebaseAuth.getInstance();

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show loading dialog
                final Dialog loadingDialog = new Dialog(ForgotPassword.this);
                loadingDialog.setContentView(R.layout.dialog_progress);
                loadingDialog.setCancelable(false);
                loadingDialog.show();

                // Send password reset email
                String email = forgetpassword.getEditText().getText().toString();
                FAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismiss(); // Dismiss loading dialog
                        if (task.isSuccessful()) {
                            ReusableCodeForAll.ShowAlert(ForgotPassword.this, "", "Password has been sent to your Email");
                            Intent intent = new Intent(ForgotPassword.this, Login.class);
                            startActivity(intent);
                        } else {
                            ReusableCodeForAll.ShowAlert(ForgotPassword.this, "Error", task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }
}
