package com.example.foodprojectapp.ChefFoodPanel.ChefLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.foodprojectapp.R;
import com.example.foodprojectapp.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ChefRegisteration extends AppCompatActivity {

    String[] provinces = {"Đà Nẵng", "Quảng Nam"};

    String[] daNangDistricts = {"Sơn Trà", "Hải Châu", "Liên Chiểu", "Ngũ Hành Sơn", "Thanh Khê", "Cẩm Lệ"};
    String[] sonTraSuburbs = {"Mỹ Khê", "Bắc Mỹ An", "An Hải Tây", "An Hải Đông", "Phước Mỹ", "Thọ Quang", "Tam Thuận"};
    String[] haiChauSuburbs = {"Phước Ninh", "Hải Châu Bắc", "Hải Châu Nam", "Bắc Mỹ An", "Nam Dương", "Thạch Thang"};
    String[] lienChieuSuburbs = {"Hòa Minh", "Khuê Mỹ", "Thanh Khê Tây", "Hòa Hiệp Bắc"};
    String[] nguHanhSonSuburbs = {"Khuê Mỹ", "Ngọc Mỹ", "Hòa Hải", "Hòa Quý"};
    String[] camLeSuburbs = {"Hòa Phát", "Hòa An", "Tam Phú", "Khải Hoàn"};

    String[] quangNamDistricts = {"Hội An", "Tam Kỳ", "Núi Thành", "Thăng Bình", "Phú Ninh", "Điện Bàn"};
    String[] hoiAnSuburbs = {"Cẩm Phô", "Tân An", "Minh An", "Cẩm Thanh", "An Bàng", "Thanh Hà", "Cẩm Kim"};
    String[] dienBanSuburbs = {"Điện Hồng", "Điện Nam Bắc", "Điện Nam Trung"};

    TextInputLayout Fname, Lname, Email, Pass, cfpass, mobileno;
    Spinner statespin, Cityspin, Suburban;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname;
    String lname;
    String emailid;
    String password;
    String confirmpassword;
    String mobile;
    String role = "Chef";
    String statee;
    String cityy;
    String suburban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_registeration);

        Fname = findViewById(R.id.Firstname);
        Lname = findViewById(R.id.Lastname);
        Email = findViewById(R.id.Email);
        Pass = findViewById(R.id.Pwd);
        cfpass = findViewById(R.id.Cpass);
        mobileno = findViewById(R.id.Mobileno);
        statespin = findViewById(R.id.Statee);
        Cityspin = findViewById(R.id.Citys);
        Suburban = findViewById(R.id.Suburban);
        signup = findViewById(R.id.Signup);
        Emaill = findViewById(R.id.emaill);
        Cpp = findViewById(R.id.CountryCode);
        Cpp.setCountryForPhoneCode(84);

        // Thiết lập Spinner cho tỉnh
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        statespin.setAdapter(stateAdapter);
        statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = provinces[position];
                statee = selectedProvince.toString().trim();
                ArrayList<String> districtList = new ArrayList<>();

                if (selectedProvince.equals("Đà Nẵng")) {
                    Collections.addAll(districtList, daNangDistricts);
                    districtList.add(selectedProvince);
                } else if (selectedProvince.equals("Quảng Nam")) {
                    Collections.addAll(districtList, quangNamDistricts);
                    districtList.add(selectedProvince);
                }

                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(ChefRegisteration.this, android.R.layout.simple_spinner_item, districtList);
                Cityspin.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Thiết lập Spinner cho quận
        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = (String) parent.getItemAtPosition(position);
                cityy = selectedDistrict.toString().trim();
                ArrayList<String> suburbList = new ArrayList<>();

                if (selectedDistrict.equals("Sơn Trà")) {
                    Collections.addAll(suburbList, sonTraSuburbs);
                    suburbList.add(selectedDistrict);
                } else if (selectedDistrict.equals("Hải Châu")) {
                    Collections.addAll(suburbList, haiChauSuburbs);
                    suburbList.add(selectedDistrict);
                } else if (selectedDistrict.equals("Hội An")) {
                    Collections.addAll(suburbList, hoiAnSuburbs);
                    suburbList.add(selectedDistrict);

                } else if (selectedDistrict.equals("Liên Chiểu")) {
                    Collections.addAll(suburbList, lienChieuSuburbs);
                    suburbList.add(selectedDistrict);
                } else if (selectedDistrict.equals("Cẩm Lệ")) {
                    Collections.addAll(suburbList, camLeSuburbs);
                    suburbList.add(selectedDistrict);
                } else if (selectedDistrict.equals("Điện Bàn")) {
                    Collections.addAll(suburbList, dienBanSuburbs);
                    suburbList.add(selectedDistrict);
                } else if (selectedDistrict.equals("Hải Châu")) {
                    Collections.addAll(suburbList, haiChauSuburbs);
                    suburbList.add(selectedDistrict);
                } else if (selectedDistrict.equals("Ngũ Hành Sơn")) {
                    Collections.addAll(suburbList, nguHanhSonSuburbs);
                    suburbList.add(selectedDistrict);
                }

                ArrayAdapter<String> suburbAdapter = new ArrayAdapter<>(ChefRegisteration.this, android.R.layout.simple_spinner_item, suburbList);
                Suburban.setAdapter(suburbAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Suburban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                suburban = value.toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Chef");
        FAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confirmpassword = cfpass.getEditText().getText().toString().trim();



                if (isValid()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefRegisteration.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_progress, null); // Bạn cần tạo layout dialog_progress.xml
                    ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
                    builder.setView(dialogView);
                    builder.setCancelable(false); // Không cho phép hủy khi chạm bên ngoài

                    AlertDialog mDialog = builder.create();
                    mDialog.show(); // Hiển thị dialog


                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMappp = new HashMap<>();
                                        hashMappp.put("City", cityy);
                                        hashMappp.put("ConfirmPassword", confirmpassword);
                                        hashMappp.put("EmailID", emailid);
                                        hashMappp.put("Fname", fname);
                                        hashMappp.put("Lname", lname);
                                        hashMappp.put("Mobile", mobile);
                                        hashMappp.put("Password", password);
                                        hashMappp.put("State", statee);
                                        hashMappp.put("Suburban", suburban);
                                        FirebaseDatabase.getInstance().getReference("Chef")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();

                                                        FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefRegisteration.this);
                                                                    builder.setMessage("Registered Successfully,Please Verify your Email");
                                                                    builder.setCancelable(false);
                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Intent loginIntent = new Intent(ChefRegisteration.this, ChefLogin.class);
                                                                            startActivity(loginIntent);
                                                                        }
                                                                    });
                                                                    AlertDialog alert = builder.create();
                                                                    alert.show();

                                                                } else {
                                                                    mDialog.dismiss();
                                                                    ReusableCodeForAll.ShowAlert(ChefRegisteration.this, "Error", task.getException().getMessage());

                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });


                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(ChefRegisteration.this, "Error", task.getException().getMessage());
                            }

                        }
                    });

                }

            }

        });

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChefRegisteration.this, ChefLogin.class);
                startActivity(i);
                finish();
            }
        });



    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cfpass.setErrorEnabled(false);
        cfpass.setError("");

        boolean isValidname = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false, isvalidlname = false;
        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Firstname is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(lname)) {
            Lname.setErrorEnabled(true);
            Lname.setError("Lastname is required");
        } else {
            isvalidlname = true;
        }
        if (TextUtils.isEmpty(emailid)) {
            Email.setErrorEnabled(true);
            Email.setError("Email is required");
        } else {
            if (emailid.matches(emailpattern)) {
                isValidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(password)) {
            Pass.setErrorEnabled(true);
            Pass.setError("Password is required");
        } else {
            if (password.length() < 6) {
                Pass.setErrorEnabled(true);
                Pass.setError("password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            cfpass.setErrorEnabled(true);
            cfpass.setError("Confirm Password is required");
        } else {
            if (!password.equals(confirmpassword)) {
                Pass.setErrorEnabled(true);
                Pass.setError("Password doesn't match");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        if (TextUtils.isEmpty(mobile)) {
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile number is required");
        } else {
            if (mobile.length() < 9) {
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }


        isvalid = isValidname && isvalidlname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno;
        return isvalid;
    }


}



