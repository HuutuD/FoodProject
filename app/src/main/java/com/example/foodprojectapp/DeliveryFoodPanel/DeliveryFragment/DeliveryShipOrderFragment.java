package com.example.foodprojectapp.DeliveryFoodPanel.DeliveryFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryModels.DeliveryShipFinalOrders1;
import com.example.foodprojectapp.DeliveryFoodPanel.DeliveryAdapter.DeliveryShipOrderFragmentAdapter;
import com.example.foodprojectapp.MainMenu;
import com.example.foodprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryShipOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeliveryShipOrderFragmentAdapter adapter;
    private DatabaseReference databaseReference;
    private List<DeliveryShipFinalOrders1> deliveryShipFinalOrders1List;
    private final String deliveryId = "iCWpMP1H52XbGvqEsmYykc6OYsN2";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliveryshiporder, container, false);
        getActivity().setTitle("Ship Orders");

        recyclerView = view.findViewById(R.id.delishiporder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        deliveryShipFinalOrders1List = new ArrayList<>();
        adapter = new DeliveryShipOrderFragmentAdapter(getContext(), deliveryShipFinalOrders1List);
        recyclerView.setAdapter(adapter);

        loadDeliveryShipFinalOrders();
        return view;
    }

    private void loadDeliveryShipFinalOrders() {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("DeliveryShipFinalOrders")
                .child(deliveryId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryShipFinalOrders1List.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot dishesSnapshot = orderSnapshot.child("Dishes");
                    for (DataSnapshot dishSnapshot : dishesSnapshot.getChildren()) {
                        DeliveryShipFinalOrders1 deliveryOrder = dishSnapshot.getValue(DeliveryShipFinalOrders1.class);

                        if (deliveryOrder != null) {
                            deliveryShipFinalOrders1List.add(deliveryOrder);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors (logging, showing messages, etc.).
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.LogOut) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
