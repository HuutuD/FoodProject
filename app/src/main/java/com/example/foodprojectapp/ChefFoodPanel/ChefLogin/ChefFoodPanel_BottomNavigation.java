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

import com.example.foodprojectapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.iid.FirebaseInstanceId;
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
        String name = getIntent().getStringExtra("PAGE");
        if (name != null) {
            if (name.equalsIgnoreCase("Orderpage")) {
                loadcheffragment(new ChefPendingOrdersFragment());
            } else if (name.equalsIgnoreCase("Confirmpage")) {
                loadcheffragment(new ChefOrderFragment());
            } else if (name.equalsIgnoreCase("AcceptOrderpage")) {
                loadcheffragment(new ChefHomeFragment());
            } else if (name.equalsIgnoreCase("Deliveredpage")) {
                loadcheffragment(new ChefHomeFragment());
            }
        } else {
            loadcheffragment(new ChefHomeFragment());
        }
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        if (menuItem.getItemId() == R.id.chefHome) {
            fragment = new ChefHomeFragment();
        } else if (menuItem.getItemId() == R.id.PendingOrders) {
            fragment = new ChefPendingOrdersFragment();
        } else if (menuItem.getItemId() == R.id.Orders) {
            fragment = new ChefOrderFragment();
        } else if (menuItem.getItemId() == R.id.chefProfile) {
            fragment = new ChefProfileFragment();
        }

        return loadcheffragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chef_bottom_navigation, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.chefHome) {
            startActivity(new Intent(this, ChefHomeFragment.class));
            return true;
        } else if (item.getItemId() == R.id.PendingOrders) {
            startActivity(new Intent(this, ChefPendingOrdersFragment.class));
            return true;
        }else if (item.getItemId() == R.id.Orders) {
            startActivity(new Intent(this, ChefOrderFragment.class));
            return true;
        }else if (item.getItemId() == R.id.chefProfile) {
            startActivity(new Intent(this, ChefProfileFragment.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}