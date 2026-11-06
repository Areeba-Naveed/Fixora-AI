package com.fixora.ai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class firstsplash extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstsplash);

        handler.postDelayed(() -> {
            Intent intent = new Intent(
                    firstsplash.this,
                    splashscreen.class
            );
            startActivity(intent);
            finish();
        }, 2000);
    }
}