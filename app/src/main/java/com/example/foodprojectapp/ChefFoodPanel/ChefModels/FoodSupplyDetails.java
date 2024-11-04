package com.example.foodprojectapp.ChefFoodPanel.ChefModels;

public class FoodSupplyDetails {

    public String Dishes,Quantity,Price,Description,RandomUID,ChefId;
    public FoodSupplyDetails() {
    }
    public FoodSupplyDetails(String dishes, String quantity, String price, String description,String randomUID,String chefId) {
        Dishes = dishes;
        Quantity = quantity;
        Price = price;
        Description = description;
        RandomUID=randomUID;
        ChefId=chefId;
    }
    @Override
    public String toString() {
        return Dishes; // Hiển thị tên món ăn trong ListView
    }
}
