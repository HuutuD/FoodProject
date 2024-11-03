package com.example.foodprojectapp.CustomerFoodPanel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerCartFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerHomeFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerOrdersFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerProfileFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerTrackFragment;
import com.example.foodprojectapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener{

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
        Fragment selectedFragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (item.getItemId() == R.id.Home && !(currentFragment instanceof CustomerHomeFragment)) {
            selectedFragment = new CustomerHomeFragment();
        } else if (item.getItemId() == R.id.Cart && !(currentFragment instanceof CustomerCartFragment)) {
            selectedFragment = new CustomerCartFragment();
        } else if (item.getItemId() == R.id.Profile && !(currentFragment instanceof CustomerProfileFragment)) {
            selectedFragment = new CustomerProfileFragment();
        } else if (item.getItemId() == R.id.Order && !(currentFragment instanceof CustomerOrdersFragment)) {
            selectedFragment = new CustomerOrdersFragment();
        } else if (item.getItemId() == R.id.Track && !(currentFragment instanceof CustomerTrackFragment)) {
            selectedFragment = new CustomerTrackFragment();
        }

        return loadFragment(selectedFragment);
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
