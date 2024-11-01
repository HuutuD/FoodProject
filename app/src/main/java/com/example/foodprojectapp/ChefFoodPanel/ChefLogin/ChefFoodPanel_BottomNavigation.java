package com.example.foodprojectapp.ChefFoodPanel.ChefLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.foodprojectapp.ChefFoodPanel.ChefFragment.ChefHomeFragment;
import com.example.foodprojectapp.ChefFoodPanel.ChefFragment.ChefPendingOrdersFragment;
import com.example.foodprojectapp.ChefFoodPanel.ChefFragment.ChefProfileFragment;
import com.example.foodprojectapp.ChefFoodPanel.ChefFragment.ChefOrderFragment;

import com.example.foodprojectapp.MainMenu;
import com.example.foodprojectapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel__bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UpdateToken();
    }

    private void UpdateToken() {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isComplete()){
                String token = task.getResult();
                FirebaseDatabase.getInstance().getReference("Tokens")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance()
                                        .getCurrentUser())
                                .getUid())
                        .setValue(token);

            }
        });
 }

    private boolean loadcheffragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                        .commit();
            return true;
        }

        return false;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (menuItem.getItemId() == R.id.chefHome) {
            if (!(currentFragment instanceof ChefHomeFragment)) {
                selectedFragment = new ChefHomeFragment();
            }
        } else if (menuItem.getItemId() == R.id.PendingOrders) {
            if (!(currentFragment instanceof ChefPendingOrdersFragment)) {
                selectedFragment = new ChefPendingOrdersFragment();
            }
        } else if (menuItem.getItemId() == R.id.Orders) {
            if (!(currentFragment instanceof ChefOrderFragment)) {
                selectedFragment = new ChefOrderFragment();
            }
        } else if (menuItem.getItemId() == R.id.chefProfile) {
            if (!(currentFragment instanceof ChefProfileFragment)) {
                selectedFragment = new ChefProfileFragment();
            }
        }

        // Chỉ thay thế fragment nếu `selectedFragment` không phải là null
        if (selectedFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        return true;
    }


}