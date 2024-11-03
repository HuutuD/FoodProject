package com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerCartFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerHomeFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerOrdersFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerProfileFragment;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment.CustomerTrackFragment;
import com.example.foodprojectapp.MainMenu;
import com.example.foodprojectapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.customer_bottom_navigation);
        navigationView.setOnItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.LogOut) {
            FirebaseAuth.getInstance().signOut();
            // Chuyển về trang LoginActivity
            Intent intent = new Intent(this, MainMenu.class); // Thay LoginActivity bằng tên Activity đăng nhập của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp back
            startActivity(intent);
            finish(); // Đóng activity hiện tại
            return true;
        } else {
            return super.onOptionsItemSelected(item);
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

        if (selectedFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
        return true;
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
