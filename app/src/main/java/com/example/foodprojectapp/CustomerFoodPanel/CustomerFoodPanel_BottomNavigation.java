package com.example.foodprojectapp.CustomerFoodPanel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodprojectapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.customer_bottom_navigation);
        navigationView.setOnItemSelectedListener(this);
        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(name != null){
            if(name.equalsIgnoreCase("Homepage")){
                loadFragment(new CustomerHomeFragment());
            }else if(name.equalsIgnoreCase("Preparingpage")){
                loadFragment(new CustomerTrackFragment());
            }else if (name.equalsIgnoreCase("DeliveryOrderpage")){
                loadFragment(new CustomerTrackFragment());
            }else if (name.equalsIgnoreCase("Thankyoupage")){
                loadFragment(new CustomerHomeFragment());
            }
        } else {
            loadFragment(new CustomerHomeFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.cust_Home) {
            fragment = new CustomerHomeFragment();
        } else if (item.getItemId() == R.id.cart) {
            fragment = new CustomerCartFragment();
        } else if (item.getItemId() == R.id.Cust_order) {
            fragment = new CustomerOrdersFragment();
        } else if (item.getItemId() == R.id.track) {
            fragment = new CustomerTrackFragment();
        } else if (item.getItemId() == R.id.cust_profile) {
            fragment = new CustomerProfileFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }

        return false;
    }
}
