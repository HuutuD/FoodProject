package com.example.foodprojectapp.CustomerFoodPanel.CustomerFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodprojectapp.CustomerFoodPanel.CustomerModels.Customer;
import com.example.foodprojectapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerProfileFragment extends Fragment {

    private String[] provinces = {"Đà Nẵng", "Quảng Nam"};
    private String[] daNangDistricts = {"Sơn Trà", "Hải Châu", "Liên Chiểu", "Ngũ Hành Sơn", "Thanh Khê", "Cẩm Lệ"};
    private String[] sonTraSuburbs = {"Mỹ Khê", "Bắc Mỹ An", "An Hải Tây", "An Hải Đông", "Phước Mỹ", "Thọ Quang", "Tam Thuận"};
    private String[] haiChauSuburbs = {"Phước Ninh", "Hải Châu Bắc", "Hải Châu Nam", "Bắc Mỹ An", "Nam Dương", "Thạch Thang"};
    private String[] lienChieuSuburbs = {"Hòa Minh", "Khuê Mỹ", "Thanh Khê Tây", "Hòa Hiệp Bắc"};
    private String[] nguHanhSonSuburbs = {"Khuê Mỹ", "Ngọc Mỹ", "Hòa Hải", "Hòa Quý"};
    private String[] camLeSuburbs = {"Hòa Phát", "Hòa An", "Tam Phú", "Khải Hoàn"};

    private String[] quangNamDistricts = {"Hội An", "Tam Kỳ", "Núi Thành", "Thăng Bình", "Phú Ninh", "Điện Bàn"};
    private String[] hoiAnSuburbs = {"Cẩm Phô", "Tân An", "Minh An", "Cẩm Thanh", "An Bàng", "Thanh Hà", "Cẩm Kim"};
    private String[] dienBanSuburbs = {"Điện Hồng", "Điện Nam Bắc", "Điện Nam Trung"};

    private EditText Fname, Lname, mobileno;
    private Spinner State, City, Suburban;
    private TextView Email, password, confirmPassword1;
    private Button Update;
    private LinearLayout LogOut;
    private DatabaseReference databaseReference;

    private String statee, cityy, suburban;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        View v = inflater.inflate(R.layout.fragment_customerprofile, null);

        // Khởi tạo các view
        Fname = v.findViewById(R.id.fnamee);
        Lname = v.findViewById(R.id.lnamee);
        Email = v.findViewById(R.id.emailID);
        State = v.findViewById(R.id.statee);
        City = v.findViewById(R.id.cityy);
        Suburban = v.findViewById(R.id.sub);
        mobileno = v.findViewById(R.id.mobilenumber);
        confirmPassword1 = v.findViewById(R.id.confirmPassword); // Thêm trường Confirm Password
        Update = v.findViewById(R.id.update);
        password = v.findViewById(R.id.password);
        LogOut = v.findViewById(R.id.logout_layout);

        // Lấy thông tin người dùng
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        loadUserData();

        // Cập nhật thông tin
        updateInformation();

        return v;
    }

    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                if (customer != null) {
                    Fname.setText(dataSnapshot.child("Fname").getValue(String.class));
                    Lname.setText(dataSnapshot.child("Lname").getValue(String.class));
                    mobileno.setText(dataSnapshot.child("Mobile").getValue(String.class));
                    Email.setText(customer.getEmailID());
                    State.setSelection(getIndexByString(State, customer.getState()));
                    password.setText(dataSnapshot.child("Password").getValue(String.class));
                    confirmPassword1.setText(dataSnapshot.child("ConfirmPassword").getValue(String.class));
                    setUpCitySpinner(customer);
                }else {
                    Toast.makeText(getContext(), "No customer data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void setUpCitySpinner(Customer customer) {
        State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statee = parent.getItemAtPosition(position).toString().trim();
                ArrayList<String> list = new ArrayList<>();
                if (statee.equals("Đà Nẵng")) {
                    for (String district : daNangDistricts) {
                        list.add(district);
                    }
                } else if (statee.equals("Quảng Nam")) {
                    for (String district : quangNamDistricts) {
                        list.add(district);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                City.setAdapter(arrayAdapter);
                City.setSelection(getIndexByString(City, customer.getCity()));
                setUpSuburbanSpinner(customer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setUpSuburbanSpinner(Customer customer) {
        City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityy = parent.getItemAtPosition(position).toString().trim();
                ArrayList<String> list = new ArrayList<>();
                switch (cityy) {
                    case "Sơn Trà":
                        for (String suburb : sonTraSuburbs) list.add(suburb);
                        break;
                    case "Hải Châu":
                        for (String suburb : haiChauSuburbs) list.add(suburb);
                        break;
                    case "Hội An":
                        for (String suburb : hoiAnSuburbs) list.add(suburb);
                        break;
                    case "Liên Chiểu":
                        for (String suburb : lienChieuSuburbs) list.add(suburb);
                        break;
                    case "Cẩm Lệ":
                        for (String suburb : camLeSuburbs) list.add(suburb);
                        break;
                    case "Điện Bàn":
                        for (String suburb : dienBanSuburbs) list.add(suburb);
                        break;
                    case "Ngũ Hành Sơn":
                        for (String suburb : nguHanhSonSuburbs) list.add(suburb);
                        break;
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                Suburban.setAdapter(arrayAdapter);
                Suburban.setSelection(getIndexByString(Suburban, customer.getSuburban()));
                suburban = customer.getSuburban(); // Cập nhật giá trị suburban
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Suburban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                suburban = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateInformation() {
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String, Object> hashMap = new HashMap<>();

                // Chỉ cập nhật các trường cần thiết
                hashMap.put("Fname", Fname.getText().toString().trim());
                hashMap.put("Lname", Lname.getText().toString().trim());
                hashMap.put("Mobile", mobileno.getText().toString().trim());
                hashMap.put("EmailID", Email.getText().toString().trim());
                hashMap.put("State", statee);
                hashMap.put("City", cityy);
                hashMap.put("Suburban", suburban);

                // Khởi tạo mật khẩu mới và mật khẩu xác nhận
                String newPassword = password.getText().toString().trim();
                String confirmPassword = confirmPassword1.getText().toString().trim();



                // Cập nhật thông tin vào Realtime Database
                databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Update Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed! Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }



    private int getIndexByString(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        return adapter.getPosition(value);
    }
}
