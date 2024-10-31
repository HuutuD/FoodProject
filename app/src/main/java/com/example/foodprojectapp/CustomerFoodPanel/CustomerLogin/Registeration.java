package com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin;

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
import android.widget.TextView;

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

public class Registeration extends AppCompatActivity {

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


    TextInputLayout fname, lname, emaill, pass, cmpass, Mobileno;
    Spinner statespin, City, Suburban;
    Button Signin, Email, Phone;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String statee;
    String cityy;
    String suburban;
    String email;
    String password;
    String firstname;
    String lastname;
    String confirmpass;
    String mobileno;
    String role = "Customer";
    CountryCodePicker Cpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
            // Tạo AlertDialog với ProgressBar
            AlertDialog.Builder builder = new AlertDialog.Builder(Registeration.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_progress, null); // Tạo layout cho dialog progress
            ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
            TextView messageTextView = dialogView.findViewById(R.id.progressMessage);
            messageTextView.setText("Registering please wait..."); // Thiết lập thông điệp

            builder.setView(dialogView);
            builder.setCancelable(false);

            AlertDialog mDialog = builder.create();

            fname = findViewById(R.id.Fname);
            lname = findViewById(R.id.Lname);
            emaill = findViewById(R.id.Emailid);
            pass = findViewById(R.id.Password);
            cmpass = findViewById(R.id.confirmpass);
            Signin = findViewById(R.id.button);
            statespin = findViewById(R.id.Statee);
            City = findViewById(R.id.Citys);
            Suburban = findViewById(R.id.Suburban);
            Mobileno = findViewById(R.id.Mobilenumber);
            Cpp = findViewById(R.id.CountryCode);
            Email = findViewById(R.id.emaill);
            Cpp.setCountryForPhoneCode(84);

            // Thiết lập Spinner cho tỉnh
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
            statespin.setAdapter(stateAdapter);
            statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedProvince = provinces[position];
                    ArrayList<String> districtList = new ArrayList<>();

                    if (selectedProvince.equals("Đà Nẵng")) {
                        Collections.addAll(districtList, daNangDistricts);
                    } else if (selectedProvince.equals("Quảng Nam")) {
                        Collections.addAll(districtList, quangNamDistricts);
                    }

                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(Registeration.this, android.R.layout.simple_spinner_item, districtList);
                    City.setAdapter(cityAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Thiết lập Spinner cho quận
            City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedDistrict = (String) parent.getItemAtPosition(position);
                    ArrayList<String> suburbList = new ArrayList<>();

                    if (selectedDistrict.equals("Sơn Trà")) {
                        Collections.addAll(suburbList, sonTraSuburbs);
                    } else if (selectedDistrict.equals("Hải Châu")) {
                        Collections.addAll(suburbList, haiChauSuburbs);
                    } else if (selectedDistrict.equals("Hội An")) {
                        Collections.addAll(suburbList, hoiAnSuburbs);

                    } else if (selectedDistrict.equals("Liên Chiểu")) {
                        Collections.addAll(suburbList, lienChieuSuburbs);
                    } else if (selectedDistrict.equals("Cẩm Lệ")) {
                        Collections.addAll(suburbList, camLeSuburbs);
                    } else if (selectedDistrict.equals("Điện Bàn")) {
                        Collections.addAll(suburbList, dienBanSuburbs);
                    } else if (selectedDistrict.equals("Hải Châu")) {
                        Collections.addAll(suburbList, haiChauSuburbs);
                    } else if (selectedDistrict.equals("Ngũ Hành Sơn")) {
                        Collections.addAll(suburbList, nguHanhSonSuburbs);
                    }

                    ArrayAdapter<String> suburbAdapter = new ArrayAdapter<>(Registeration.this, android.R.layout.simple_spinner_item, suburbList);
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


            // Khởi tạo Firebase
            databaseReference = FirebaseDatabase.getInstance().getReference("Customer");
            FAuth = FirebaseAuth.getInstance();

            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = emaill.getEditText().getText().toString().trim();
                    password = pass.getEditText().getText().toString().trim();
                    firstname = fname.getEditText().getText().toString().trim();
                    lastname = lname.getEditText().getText().toString().trim();
                    confirmpass = cmpass.getEditText().getText().toString().trim();
                    mobileno = Mobileno.getEditText().getText().toString().trim();

                    if (isValid()) {

                        TextView messageTextView = dialogView.findViewById(R.id.progressMessage);
                        messageTextView.setText("Logging in..."); // Thiết lập thông điệp

                        builder.setView(dialogView);
                        builder.setCancelable(false);  // Không cho phép hủy khi chạm bên ngoài

                        AlertDialog mDialog = builder.create();
                        mDialog.show(); // Hiển thị dialog

                        FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                    final HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("Role", role);
                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            HashMap<String, String> hashMappp = new HashMap<>();
                                            hashMappp.put("City", cityy);
                                            hashMappp.put("ConfirmPassword", confirmpass);
                                            hashMappp.put("EmailID", email);
                                            hashMappp.put("FirstName", firstname);
                                            hashMappp.put("LastName", lastname);
                                            hashMappp.put("Mobileno", mobileno);
                                            hashMappp.put("Password", password);
                                            hashMappp.put("State", statee);
                                            hashMappp.put("Suburban", suburban);
                                            FirebaseDatabase.getInstance().getReference("Customer")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            mDialog.dismiss();

                                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Registeration.this);
                                                                        builder.setMessage("Registered Successfully,Please Verify your Email");
                                                                        builder.setCancelable(false);
                                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Intent loginIntent = new Intent(Registeration.this, Login.class);
                                                                                startActivity(loginIntent);
                                                                            }
                                                                        });
                                                                        AlertDialog alert = builder.create();
                                                                        alert.show();

                                                                    } else {
                                                                        mDialog.dismiss();
                                                                        ReusableCodeForAll.ShowAlert(Registeration.this, "Error", task.getException().getMessage());

                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    });

                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(Registeration.this, "Error", task.getException().getMessage());
                                }
                            }
                        });
                    }


                }
            });



        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Registeration.this, Login.class);
                startActivity(i);
                finish();
            }
        });




    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        emaill.setErrorEnabled(false);
        emaill.setError("");
        fname.setErrorEnabled(false);
        fname.setError("");
        lname.setErrorEnabled(false);
        lname.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");
        cmpass.setErrorEnabled(false);
        cmpass.setError("");
        Mobileno.setErrorEnabled(false);
        Mobileno.setError("");

        boolean isValidfirstname = false, isValidlastname = false, isValidaddress = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false;
        if (TextUtils.isEmpty(firstname)) {
            fname.setErrorEnabled(true);
            fname.setError("FirstName is required");
        } else {
            isValidfirstname = true;
        }
        if (TextUtils.isEmpty(lastname)) {
            lname.setErrorEnabled(true);
            lname.setError("LastName is required");
        } else {
            isValidlastname = true;
        }
        if (TextUtils.isEmpty(email)) {
            emaill.setErrorEnabled(true);
            emaill.setError("Email is required");
        } else {
            if (email.matches(emailpattern)) {
                isValidemail = true;
            } else {
                emaill.setErrorEnabled(true);
                emaill.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(mobileno)) {
            Mobileno.setErrorEnabled(true);
            Mobileno.setError("Mobile number is required");
        } else {
            if (mobileno.length() < 9) {
                Mobileno.setErrorEnabled(true);
                Mobileno.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(password)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            if (password.length() < 6) {
                pass.setErrorEnabled(true);
                pass.setError("Password too weak");
                cmpass.setError("password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confirmpass)) {
            cmpass.setErrorEnabled(true);
            cmpass.setError("Confirm Password is required");
        } else {
            if (!password.equals(confirmpass)) {
                pass.setErrorEnabled(true);
                pass.setError("Password doesn't match");
                cmpass.setError("Password doesn't match");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        isvalid = isValidfirstname && isValidlastname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno && isValidaddress;
        return isvalid;
    }
}

