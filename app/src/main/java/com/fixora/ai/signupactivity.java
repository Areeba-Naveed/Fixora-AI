package com.fixora.ai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signupactivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etPassword, etCity;
    private TextView btnCustomer, btnProvider, btnSignUp, btnBack, tvLogin;
    private LinearLayout providerFields;
    private Spinner spServiceType;

    private String selectedRole = "Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCity = findViewById(R.id.etCity);

        btnCustomer = findViewById(R.id.btnCustomer);
        btnProvider = findViewById(R.id.btnProvider);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBack = findViewById(R.id.btnBack);
        tvLogin = findViewById(R.id.tvLogin);

        providerFields = findViewById(R.id.providerFields);
        spServiceType = findViewById(R.id.spServiceType);

        // Service types
        String[] services = {
                "Select Service Type",
                "Electrician",
                "AC Technician",
                "Plumber",
                "Appliance Repair",
                "Carpenter",
                "Painter"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                services
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spServiceType.setAdapter(adapter);

        // Default role
        selectCustomer();

        btnCustomer.setOnClickListener(v -> selectCustomer());
        btnProvider.setOnClickListener(v -> selectProvider());

        btnSignUp.setOnClickListener(v -> registerUser());

        btnBack.setOnClickListener(v -> finish());

        tvLogin.setOnClickListener(v -> openLogin());
    }

    private void selectCustomer() {
        selectedRole = "Customer";

        providerFields.setVisibility(View.GONE);

        btnCustomer.setBackgroundResource(R.drawable.bg_button_primary);
        btnCustomer.setTextColor(0xFFFFFFFF);

        btnProvider.setBackgroundResource(android.R.color.transparent);
        btnProvider.setTextColor(0xFF55516B);
    }

    private void selectProvider() {
        selectedRole = "Service Provider";

        providerFields.setVisibility(View.VISIBLE);

        btnProvider.setBackgroundResource(R.drawable.bg_button_primary);
        btnProvider.setTextColor(0xFFFFFFFF);

        btnCustomer.setBackgroundResource(android.R.color.transparent);
        btnCustomer.setTextColor(0xFF55516B);
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()
                || email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        String serviceType = "";
        String city = "";

        if (selectedRole.equals("Service Provider")) {

            serviceType = spServiceType.getSelectedItem().toString();
            city = etCity.getText().toString().trim();

            if (spServiceType.getSelectedItemPosition() == 0) {
                Toast.makeText(this,
                        "Please select your service type",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (city.isEmpty()) {
                etCity.setError("Enter your city or area");
                return;
            }
        }

        // Demo registration data
        SharedPreferences preferences =
                getSharedPreferences("FixoraUsers", MODE_PRIVATE);

        preferences.edit()
                .putString("name", name)
                .putString("phone", phone)
                .putString("email", email)
                .putString("password", password)
                .putString("role", selectedRole)
                .putString("serviceType", serviceType)
                .putString("city", city)
                .apply();

        Toast.makeText(this,
                "Account created successfully. Please login.",
                Toast.LENGTH_LONG).show();

        // Signup ke baad Login page open hoga
        openLogin();
    }

    private void openLogin() {
        // Agar tumhari login class ka naam different hai,
        // to yahan us class ka actual naam use karna.
        Intent intent = new Intent(signupactivity.this, loginscreen.class);
        startActivity(intent);
        finish();
    }
}
