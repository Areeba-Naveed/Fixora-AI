package com.fixora.ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class homeactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);

        // Service cards
        setServiceClick(R.id.svcElectrician);
        setServiceClick(R.id.svcAC);
        setServiceClick(R.id.svcPlumber);
        setServiceClick(R.id.svcAppliance);
        setServiceClick(R.id.svcCarpenter);
        setServiceClick(R.id.svcPainter);

        // AI Diagnosis card aur Try Now button
        View.OnClickListener aiClick = v ->
                startActivity(new Intent(this, aidiagnosisactivity.class));
        findViewById(R.id.cardAIDiagnosis).setOnClickListener(aiClick);
        findViewById(R.id.btnTryNow).setOnClickListener(aiClick);

        // Notification
        findViewById(R.id.btnNotification).setOnClickListener(v ->
                Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show());

        // Bottom navigation
        findViewById(R.id.navHome).setOnClickListener(v -> { });
        // Bookings screen
        findViewById(R.id.navBookings).setOnClickListener(v -> {
            Intent intent = new Intent(homeactivity.this, customerbooking.class);
            startActivity(intent);
        });

// Messages screen
        findViewById(R.id.navMessages).setOnClickListener(v -> {
            Intent intent = new Intent(homeactivity.this, customermessage.class);
            startActivity(intent);
        });
        findViewById(R.id.navProfile).setOnClickListener(v -> {
            Intent intent = new Intent(homeactivity.this,
                    profileactivity.class);
            startActivity(intent);
        });
    }

    private void setServiceClick(int id) {
        View card = findViewById(id);
        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, serviceactivity.class);
            intent.putExtra("service", (String) v.getTag());
            startActivity(intent);
        });
    }}