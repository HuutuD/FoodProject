package com.example.foodprojectapp.ChefFoodPanel.ChefFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodprojectapp.ChefFoodPanel.ChefOrderTobePrepared;
import com.example.foodprojectapp.ChefFoodPanel.ChefPreparedOrder;
import com.example.foodprojectapp.R;
public class ChefOrderFragment extends Fragment {

    TextView OrdertobePrepare, Preparedorders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Orders");
        View v = inflater.inflate(R.layout.fragment_chef_order, null);
        OrdertobePrepare= v.findViewById(R.id.ordertobe);
        Preparedorders= v.findViewById(R.id.prepareorder);

        OrdertobePrepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), ChefOrderTobePrepared.class);
                startActivity(i);
            }
        });

        Preparedorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ChefPreparedOrder.class);
                startActivity(intent);
            }
        });


        return v;
    }
}
