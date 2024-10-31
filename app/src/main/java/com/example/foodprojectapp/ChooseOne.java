package com.example.foodprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodprojectapp.ChefFoodPanel.ChefLogin.ChefLogin;
import com.example.foodprojectapp.ChefFoodPanel.ChefLogin.ChefRegisteration;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin.Login;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin.Registeration;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryLogin.Delivery_Login;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryLogin.Delivery_registeration;

public class ChooseOne extends AppCompatActivity {

    Button Chef, Customer, DeliveryPerson;
    Intent intent;
    String type;
    ConstraintLayout bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);
        Chef = findViewById(R.id.chef);
        DeliveryPerson = findViewById(R.id.delivery);
        Customer = findViewById(R.id.customer);

        intent = getIntent();
        type = intent.getStringExtra("Home").trim();

        Chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")) {
                    Intent loginemail = new Intent(ChooseOne.this, ChefLogin.class);
                    startActivity(loginemail);
                    finish();
                }

                if (type.equals("SignUp")) {
                    Intent Register = new Intent(ChooseOne.this, ChefRegisteration.class);
                    startActivity(Register);


                }

            }
        });

        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")) {
                    Intent loginemailcust = new Intent(ChooseOne.this, Login.class);
                    startActivity(loginemailcust);
                    finish();
                }

                if (type.equals("SignUp")) {
                    Intent Registercust = new Intent(ChooseOne.this, Registeration.class);
                    startActivity(Registercust);
                }
            }
        });

        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("SignUp")) {
                    Intent Registerdelivery = new Intent(ChooseOne.this, Delivery_registeration.class);
                    startActivity(Registerdelivery);
                }

                if (type.equals("Email")) {
                    Intent loginemail = new Intent(ChooseOne.this, Delivery_Login.class);
                    startActivity(loginemail);
                    finish();
                }
            }
        });
    }
}