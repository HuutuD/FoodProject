package com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodprojectapp.ChefFoodPanel.UpdateDishModel;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerAdapter.CustomerHomeAdapter;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerModels.Customer;

import com.example.foodprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private CustomerHomeAdapter adapter;
    private String State, City, Sub;
    private DatabaseReference customerReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customerhome, container, false);
//        getActivity().setTitle("Home");
        setHasOptionsMenu(true);
        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();

        // Lấy thông tin người dùng
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        customerReference = FirebaseDatabase.getInstance().getReference("Customer").child(userId);

        customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customerfc = dataSnapshot.getValue(Customer.class);
                if (customerfc != null) {
                    State = customerfc.getState();
                    City = customerfc.getCity();
                    Sub = customerfc.getSuburban();
                    loadDishes();
                } else {
                    Log.d("CustomerHomeFragment", "Customer data is null.");
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu khách hàng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CustomerHomeFragment", "Database error: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Lỗi.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void loadDishes() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("FoodSupplyDetails")
                .child(State)
                .child(City)
                .child(Sub);

        // Lấy dữ liệu theo chefId
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList.clear();
                for (DataSnapshot chefSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot dishSnapshot : chefSnapshot.getChildren()) {
                        UpdateDishModel updateDishModel = dishSnapshot.getValue(UpdateDishModel.class);
                        if (updateDishModel != null) {
                            updateDishModelList.add(updateDishModel);
                        }
                    }
                }
                adapter = new CustomerHomeAdapter(getContext(), updateDishModelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CustomerHomeFragment", "Database error: " + databaseError.getMessage());
            }
        });
    }
}

