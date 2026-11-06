package com.fixora.ai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Button btnGetStarted = findViewById(R.id.btnGetStarted);


        btnGetStarted.setOnClickListener(v -> openMainScreen());

    }

    private void openMainScreen() {
        Intent intent = new Intent(splashscreen.this, Activitymain.class);
        startActivity(intent);
    }
}
