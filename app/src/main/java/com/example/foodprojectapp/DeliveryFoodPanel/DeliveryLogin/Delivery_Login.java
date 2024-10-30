package com.example.foodprojectapp.DeliveryFoodPanel.DeliveryLogin;

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

public class Delivery_Login extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView Forgotpassword;
    TextView txt;
    FirebaseAuth FAuth;
    String em;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__login);

        email = findViewById(R.id.Demail);
        pass = findViewById(R.id.Dpassword);
        Signin = findViewById(R.id.Loginbtn);
        txt = findViewById(R.id.donot);
        Forgotpassword = findViewById(R.id.Dforgotpass);
        FAuth = FirebaseAuth.getInstance();

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                em = email.getEditText().getText().toString().trim();
                pwd = pass.getEditText().getText().toString().trim();
                if (isValid()) {
                    // Tạo AlertDialog với ProgressBar
                    AlertDialog.Builder builder = new AlertDialog.Builder(Delivery_Login.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_progress, null); // Tạo layout cho dialog progress
                    ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
                    TextView messageTextView = dialogView.findViewById(R.id.progressMessage);
                    messageTextView.setText("Logging in..."); // Thiết lập thông điệp

                    builder.setView(dialogView);
                    builder.setCancelable(false);  // Không cho phép hủy khi chạm bên ngoài

                    AlertDialog mDialog = builder.create();
                    mDialog.show(); // Hiển thị dialog

                    FAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mDialog.dismiss(); // Tắt dialog khi hoàn thành
                            if (task.isSuccessful()) {
                                if (FAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(Delivery_Login.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                    Intent z = new Intent(Delivery_Login.this, Delivery_FoodPanelBottomNavigation.class);
                                    startActivity(z);
                                    finish();
                                } else {
                                    ReusableCodeForAll.ShowAlert(Delivery_Login.this, "", "Please Verify your Email");
                                }
                            } else {
                                ReusableCodeForAll.ShowAlert(Delivery_Login.this, "Error", task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Register = new Intent(Delivery_Login.this, Delivery_registeration.class);
                startActivity(Register);
                finish();
            }
        });

        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Delivery_Login.this, Delivery_ForgotPassword.class);
                startActivity(a);
                finish();
            }
        });


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
