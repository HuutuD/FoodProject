package com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodprojectapp.ChefFoodPanel.ChefAdapter.ChefhomeAdapter;
import com.example.foodprojectapp.ChefFoodPanel.ChefModels.Chef;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerAdapter.CustomerHomeAdapter;
import com.example.foodprojectapp.CustomerFoodPanel.CustomerModels.Customer;
import com.example.foodprojectapp.ChefFoodPanel.UpdateDishModel;
import com.example.foodprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private CustomerHomeAdapter adapter;
    String State, City, Sub;
    DatabaseReference dataa, databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerhome, null);
        getActivity().setTitle("Home");
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();


        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);

        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cus = dataSnapshot.getValue(Customer.class);
                State = cus.getState();
                City = cus.getCity();
                Sub = cus.getSuburban();
                customerMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    @Override
    public void onRefresh() {
        customerMenu();
    }

    private void customerMenu() {
        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("FoodSupplyDetails")
                .child(State != null ? State : "DefaultState")
                .child(City != null ? City : "DefaultCity")
                .child(Sub != null ? Sub : "DefaultArea");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateDishModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        UpdateDishModel updateDishModel = snapshot2.getValue(UpdateDishModel.class);
                        if (updateDishModel != null) {
                            updateDishModelList.add(updateDishModel);
                        }
                    }
                }
                if (adapter == null) {
                    adapter = new CustomerHomeAdapter(getContext(), updateDishModelList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    private void search(final String searchtext) {
        ArrayList<UpdateDishModel> mylist = new ArrayList<>();
        for (UpdateDishModel object : updateDishModelList) {
            if (object.getDishes().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }

        adapter = new CustomerHomeAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);
    }
}
