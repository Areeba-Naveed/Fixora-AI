package com.fixora.ai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginscreen extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        TextView btnLogin = findViewById(R.id.btnLogin);
        TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);

        btnLogin.setOnClickListener(v -> loginUser());

        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(
                    loginscreen.this,
                    signupactivity.class
            );
            startActivity(intent);
        });
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Enter your email address");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Enter your password");
            etPassword.requestFocus();
            return;
        }

        // Signup ke waqt save kiya gaya account data
        SharedPreferences preferences =
                getSharedPreferences("FixoraUsers", MODE_PRIVATE);

        String savedEmail = preferences.getString("email", "");
        String savedPassword = preferences.getString("password", "");
        String savedRole = preferences.getString("role", "");

        // Email aur password check
        if (email.equals(savedEmail)
                && password.equals(savedPassword)) {

            Toast.makeText(
                    loginscreen.this,
                    "Login successful!",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent;

            // Customer dashboard
            if (savedRole.equals("Customer")) {

                intent = new Intent(
                        loginscreen.this,
                        homeactivity.class
                );

            }
            // Service Provider dashboard
            else if (savedRole.equals("Service Provider")) {

                intent = new Intent(
                        loginscreen.this,
                        servicehome.class
                );

            } else {
                Toast.makeText(
                        this,
                        "Account role not found",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    loginscreen.this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
