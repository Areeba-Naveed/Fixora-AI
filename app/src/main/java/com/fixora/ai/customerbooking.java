package com.fixora.ai;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class customerbooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerbooking);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
