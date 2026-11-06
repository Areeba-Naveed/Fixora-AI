package com.fixora.ai;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class airesultactivity extends AppCompatActivity {

    private ImageView ivResult;
    private TextView tvProblem;
    private TextView tvConfidence;
    private TextView tvTip1, tvTip2, tvTip3;
    private TextView tvService, tvPrice, tvServiceIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airesultactivity);

        // Connect XML views
        ivResult = findViewById(R.id.ivResult);
        tvProblem = findViewById(R.id.tvProblem);
        tvConfidence = findViewById(R.id.tvConfidence);

        tvTip1 = findViewById(R.id.tvTip1);
        tvTip2 = findViewById(R.id.tvTip2);
        tvTip3 = findViewById(R.id.tvTip3);

        tvService = findViewById(R.id.tvService);
        tvPrice = findViewById(R.id.tvPrice);
        tvServiceIcon = findViewById(R.id.tvServiceIcon);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Show selected image
        showSelectedImage();

        // Read actual AI response from backend
        String json = getIntent().getStringExtra("diagnosis_json");

        if (json == null || json.trim().isEmpty()) {
            showErrorResult();
            return;
        }

        try {
            JSONObject result = new JSONObject(json);
            displayDiagnosis(result);

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Unable to read AI result.",
                    Toast.LENGTH_LONG
            ).show();

            showErrorResult();
        }

        // Find Experts button
        findViewById(R.id.btnFindExperts).setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Available Technicians screen is the next step.",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void showSelectedImage() {

        String imageUri = getIntent().getStringExtra("image_uri");

        if (imageUri != null && !imageUri.isEmpty()) {

            try {
                ivResult.setImageURI(Uri.parse(imageUri));
                ivResult.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                showBitmapImage();
            }

        } else {
            showBitmapImage();
        }
    }

    private void showBitmapImage() {

        if (aidiagnosisactivity.sharedBitmap != null) {

            ivResult.setImageBitmap(
                    aidiagnosisactivity.sharedBitmap
            );

            ivResult.setVisibility(View.VISIBLE);

        } else {
            ivResult.setVisibility(View.GONE);
        }
    }

    private void displayDiagnosis(JSONObject result) {

        boolean problemDetected =
                result.optBoolean("problem_detected", false);

        String problem =
                result.optString("problem", "No clear problem identified");

        String confidence =
                result.optString("confidence", "Not available");

        String explanation =
                result.optString("explanation", "");

        String service =
                result.optString("recommended_service", "Not identified");

        String urgency =
                result.optString("urgency", "Not available");

        // Problem heading
        if (problemDetected) {
            tvProblem.setText(problem);
        } else {
            tvProblem.setText("No visible problem detected");
        }

        // Confidence
        tvConfidence.setText("Confidence: " + confidence);

        // First tip: explanation
        if (explanation.isEmpty()) {
            tvTip1.setText("No additional explanation available.");
        } else {
            tvTip1.setText("✓  " + explanation);
        }

        // Second tip: possible causes
        JSONArray causes = result.optJSONArray("possible_causes");

        if (causes != null && causes.length() > 0) {
            tvTip2.setText("Possible causes: " + joinArray(causes));
        } else {
            tvTip2.setText("✓  Possible causes could not be identified.");
        }

        // Third tip: safety advice
        JSONArray safety = result.optJSONArray("safety_advice");

        if (safety != null && safety.length() > 0) {
            tvTip3.setText("Safety: " + joinArray(safety));
        } else {
            tvTip3.setText("✓  Follow appropriate safety precautions.");
        }

        // Recommended service
        if (service.equalsIgnoreCase("None")
                || service.trim().isEmpty()) {
            tvService.setText("No technician recommendation");
        } else {
            tvService.setText(service);
        }

        // No fake price
        tvPrice.setText("Price estimate not available");

        // Service icon
        tvServiceIcon.setText(getServiceIcon(service));

        // Urgency
        if (!urgency.isEmpty()) {
            tvConfidence.setText(
                    "Confidence: " + confidence + "  |  Urgency: " + urgency
            );
        }
    }

    private String joinArray(JSONArray array) {

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {

            if (i > 0) {
                text.append(" • ");
            }

            text.append(array.optString(i));
        }

        return text.toString();
    }

    private String getServiceIcon(String service) {

        String value = service.toLowerCase();

        if (value.contains("electric")) {
            return "⚡";
        }

        if (value.contains("plumb")) {
            return "🚰";
        }

        if (value.contains("ac") || value.contains("air")) {
            return "❄";
        }

        if (value.contains("carpenter")) {
            return "🪑";
        }

        if (value.contains("paint")) {
            return "🎨";
        }

        if (value.contains("appliance")
                || value.contains("refrigerator")) {
            return "🔧";
        }

        return "🛠";
    }

    private void showErrorResult() {

        tvProblem.setText("Diagnosis unavailable");

        tvConfidence.setText("Confidence: Not available");

        tvTip1.setText("Please try uploading your photo again.");

        tvTip2.setText("Make sure your internet connection is working.");

        tvTip3.setText("You can also describe the problem in the app.");

        tvService.setText("Not available");

        tvPrice.setText("Price estimate not available");

        tvServiceIcon.setText("🛠");
    }
}
