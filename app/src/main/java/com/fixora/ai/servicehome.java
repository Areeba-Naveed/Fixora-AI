package com.fixora.ai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class servicehome extends AppCompatActivity {

    private TextView tvProviderName, tvServiceType, tvProviderCity;
    private TextView tvNewRequests, tvActiveJobs;

    private View requestCard1, requestCard2;

    private int newRequests = 2;
    private int activeJobs = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicehome);

        // Connect XML views
        tvProviderName = findViewById(R.id.tvProviderName);
        tvServiceType = findViewById(R.id.tvServiceType);
        tvProviderCity = findViewById(R.id.tvProviderCity);

        tvNewRequests = findViewById(R.id.tvNewRequests);
        tvActiveJobs = findViewById(R.id.tvActiveJobs);

        requestCard1 = findViewById(R.id.requestCard1);
        requestCard2 = findViewById(R.id.requestCard2);

        // Load provider information saved during signup
        SharedPreferences preferences =
                getSharedPreferences("FixoraUsers", MODE_PRIVATE);

        String name = preferences.getString("name", "Service Provider");
        String service = preferences.getString("serviceType", "Not Selected");
        String city = preferences.getString("city", "Not Provided");

        tvProviderName.setText("Welcome, " + name);
        tvServiceType.setText(service);
        tvProviderCity.setText(city);

        // Set dashboard numbers
        updateStatistics();

        // Accept and Reject Request 1
        findViewById(R.id.btnAccept1).setOnClickListener(v -> {
            acceptRequest(requestCard1, "Fan Repair");
        });

        findViewById(R.id.btnReject1).setOnClickListener(v -> {
            rejectRequest(requestCard1, "Fan Repair");
        });

        // Accept and Reject Request 2
        findViewById(R.id.btnAccept2).setOnClickListener(v -> {
            acceptRequest(requestCard2, "Electrical Wiring");
        });

        findViewById(R.id.btnReject2).setOnClickListener(v -> {
            rejectRequest(requestCard2, "Electrical Wiring");
        });

        // Notification
        findViewById(R.id.btnProviderNotification)
                .setOnClickListener(v ->
                        Toast.makeText(
                                servicehome.this,
                                "No new notifications",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private void acceptRequest(View requestCard, String serviceName) {

        if (requestCard.getVisibility() != View.VISIBLE) {
            return;
        }

        requestCard.setVisibility(View.GONE);

        newRequests--;

        activeJobs++;

        updateStatistics();

        Toast.makeText(
                this,
                serviceName + " request accepted!",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void rejectRequest(View requestCard, String serviceName) {

        if (requestCard.getVisibility() != View.VISIBLE) {
            return;
        }

        requestCard.setVisibility(View.GONE);

        newRequests--;

        updateStatistics();

        Toast.makeText(
                this,
                serviceName + " request rejected!",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void updateStatistics() {

        tvNewRequests.setText(String.valueOf(newRequests));
        tvActiveJobs.setText(String.valueOf(activeJobs));
    }
}
