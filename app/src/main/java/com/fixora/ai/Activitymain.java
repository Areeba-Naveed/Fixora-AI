package com.fixora.ai;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activitymain extends AppCompatActivity {

    private ImageView imgScene;
    private TextView tvTitle, tvDesc, btnNext;
    private LinearLayout dotsContainer;

    private int currentPage = 0;

    // Four screen headings
    private final String[] titles = {
            "Get the Right Service\nat Your Doorstep",
            "Find Trusted\nProfessionals",
            "Easy Home\nRepairs",
            "Smart Solutions\nfor Your Home"
    };

    // Four screen descriptions
    private final String[] descriptions = {
            "Electricity issue? AC not working?\nPlumbing problem? We've got you covered!",
            "Connect with skilled professionals\nfor reliable home services.",
            "Get help with AC, plumbing,\nelectricity and home repairs.",
            "Let Fixora AI help you find\nthe right solution for your home."
    };

    // Four pictures
    private final int[] images = {
            R.drawable.activitymain,
            R.drawable.seconds,
            R.drawable.third,
            R.drawable.fourth
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // White status bar
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        // Connect XML views
        imgScene = findViewById(R.id.imgScene);
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        btnNext = findViewById(R.id.btnNext);
        dotsContainer = findViewById(R.id.dotsContainer);

        View btnSkip = findViewById(R.id.btnSkip);

        // Next button
        btnNext.setOnClickListener(v -> goToNext());

        // Skip button
        btnSkip.setOnClickListener(v -> finishOnboarding());

        // Show first screen
        updatePage();
    }

    private void goToNext() {

        if (currentPage < images.length - 1) {

            currentPage++;

            updatePage();

        } else {

            finishOnboarding();
        }
    }

    private void updatePage() {

        // Change picture
        imgScene.setImageResource(images[currentPage]);

        // Change heading
        tvTitle.setText(titles[currentPage]);

        // Change description
        tvDesc.setText(descriptions[currentPage]);

        // Change button text
        if (currentPage == images.length - 1) {
            btnNext.setText("Get Started");
        } else {
            btnNext.setText("Next");
        }

        // Update page dots
        for (int i = 0; i < dotsContainer.getChildCount(); i++) {

            View dot = dotsContainer.getChildAt(i);

            if (i == currentPage) {
                dot.setBackgroundResource(R.drawable.dot_active);
            } else {
                dot.setBackgroundResource(R.drawable.dot_inactive);
            }
        }
    }
    private void finishOnboarding() {
        Intent intent = new Intent(
                Activitymain.this,
            loginscreen.class
        );

        startActivity(intent);
        finish();
    }

}