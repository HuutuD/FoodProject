package com.example.foodprojectapp.ChefFoodPanel.ChefLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodprojectapp.R;
import com.example.foodprojectapp.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ChefLogin extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signout, Signinphone;
    TextView Forgotpassword;
    TextView txt;
    FirebaseAuth FAuth;
    String em;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_login);

        try {
            email = findViewById(R.id.Lemail);
            pass = findViewById(R.id.Lpassword);
            Signout = findViewById(R.id.button4);
            txt = findViewById(R.id.textView3);
            Forgotpassword = findViewById(R.id.forgotpass);

            FAuth = FirebaseAuth.getInstance();

            Signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    em = email.getEditText().getText().toString().trim();
                    pwd = pass.getEditText().getText().toString().trim();
                    if (isValid()) {

                        // Tạo AlertDialog với ProgressBar
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChefLogin.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_progress, null);
                        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

                        builder.setView(dialogView);
                        builder.setCancelable(false);  // không cho phép hủy khi chạm bên ngoài

                        AlertDialog mDialog = builder.create();
                        mDialog.show();  // Hiện dialog

                        FAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mDialog.dismiss();  // Đóng dialog

                                if (task.isSuccessful()) {
                                    if (FAuth.getCurrentUser().isEmailVerified()) {
                                        Toast.makeText(ChefLogin.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                        Intent z = new Intent(ChefLogin.this, ChefFoodPanel_BottomNavigation.class);
                                        startActivity(z);
                                        finish();
                                    } else {
                                        ReusableCodeForAll.ShowAlert(ChefLogin.this, "", "Please Verify your Email");
                                    }
                                } else {
                                    ReusableCodeForAll.ShowAlert(ChefLogin.this, "Error", task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            });

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Register = new Intent(ChefLogin.this, ChefRegisteration.class);
                    startActivity(Register);
                    finish();
                }
            });

            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(ChefLogin.this, ChefForgotPassword.class);
                    startActivity(a);
                    finish();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalidemail = false, isvalidpassword = false;
        if (TextUtils.isEmpty(em)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            if (em.matches(emailpattern)) {
                isvalidemail = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Enter a valid Email Address");
            }
        }
        if (TextUtils.isEmpty(pwd)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            isvalidpassword = true;
        }
        return isvalidemail && isvalidpassword;
    }
}
