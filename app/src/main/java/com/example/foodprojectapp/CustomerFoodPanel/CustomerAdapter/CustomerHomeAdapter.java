package com.example.foodprojectapp.CustomerFoodPanel.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodprojectapp.CustomerFoodPanel.OrderDish;
import com.example.foodprojectapp.CustomerFoodPanel.UpdateDishModel;
import com.example.foodprojectapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel> updateDishModelList;
    DatabaseReference databaseReference;

    public CustomerHomeAdapter(Context context, List<UpdateDishModel> updateDishModelList){
        this.updateDishModelList = updateDishModelList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public CustomerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish, parent, false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel= updateDishModelList.get(position);
        Glide.with(mcontext).load(updateDishModel.getImageURL()).into(holder.imageView);
        holder.dishName.setText(updateDishModel.getDishes());
        updateDishModel.getRandomUID();
        updateDishModel.getChefId();
        holder.price.setText("Price: ₹ " + updateDishModel.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, OrderDish.class);
                intent.putExtra("FoodMenu",updateDishModel.getRandomUID());
                intent.putExtra("ChefId",updateDishModel.getChefId());

                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateDishModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView dishName, price, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.menu_image);
            dishName = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
            quantity = itemView.findViewById(R.id.tv_quantity);

            quantity.setText("1");
        }
    }
}