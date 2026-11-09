
package com.fixora.ai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.SharedPreferences;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class profileactivity extends AppCompatActivity {

    private TextView tvProfileEmail;

    private TextView tvProfileName, tvProfileInitial;
    private ImageView ivProfilePhoto;
    private SharedPreferences preferences;
    private ActivityResultLauncher<String[]> imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        preferences = getSharedPreferences("FixoraProfile", MODE_PRIVATE);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileInitial = findViewById(R.id.tvProfileInitial);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        SharedPreferences profilePrefs =
                getSharedPreferences("FixoraProfile", MODE_PRIVATE);

        String email = profilePrefs.getString("profileEmail", "");

        tvProfileEmail.setText(email);

        imagePicker = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            preferences.edit()
                                    .putString("profileImage", uri.toString())
                                    .apply();

                            showProfileImage(uri);

                        } catch (Exception e) {
                            Toast.makeText(this,
                                    "Unable to load image",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Load saved name
        String savedName = preferences.getString(
                "profileName", "Customer Name");

        tvProfileName.setText(savedName);
        tvProfileInitial.setText(
                savedName.isEmpty() ? "M" :
                        savedName.substring(0, 1).toUpperCase()
        );

        // Load saved picture
        String savedImage = preferences.getString("profileImage", null);

        if (savedImage != null) {
            showProfileImage(Uri.parse(savedImage));
        }

        // Change Profile Picture
        findViewById(R.id.profileAvatar).setOnClickListener(v -> {

            String[] options = {"Change Photo", "Remove Photo"};

            new AlertDialog.Builder(this)
                    .setTitle("Profile Picture")
                    .setItems(options, (dialog, which) -> {

                        if (which == 0) {
                            // Change Photo
                            imagePicker.launch(new String[]{"image/*"});

                        } else {
                            // Remove Photo
                            preferences.edit()
                                    .remove("profileImage")
                                    .apply();

                            ivProfilePhoto.setImageURI(null);
                            ivProfilePhoto.setVisibility(ImageView.GONE);
                            tvProfileInitial.setVisibility(TextView.VISIBLE);

                            Toast.makeText(this,
                                    "Profile picture removed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });

        // Edit Customer Name
        findViewById(R.id.btnEditProfile).setOnClickListener(v ->
                editProfileName()
        );

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // My Bookings
        findViewById(R.id.optionBookings).setOnClickListener(v ->
                startActivity(new Intent(this, customerbooking.class)));

        // Notifications
        findViewById(R.id.optionNotifications).setOnClickListener(v -> {
            Intent intent = new Intent(
                    profileactivity.this,
                    notificationsactivity.class
            );
            startActivity(intent);
        });
        // Payment Methods
        findViewById(R.id.optionPayment).setOnClickListener(v -> {
            Intent intent = new Intent(
                    profileactivity.this,
                    PaymentMethodsActivity.class
            );
            startActivity(intent);
        });

        // Help & Support
        findViewById(R.id.optionSupport).setOnClickListener(v -> {
            Intent intent = new Intent(
                    profileactivity.this,
                    HelpSupportActivity.class
            );
            startActivity(intent);
        });

        // Settings
        findViewById(R.id.optionSettings).setOnClickListener(v -> {
            Intent intent = new Intent(
                    profileactivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, homeactivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.navBookings).setOnClickListener(v ->
                startActivity(new Intent(this, customerbooking.class)));

        findViewById(R.id.navMessages).setOnClickListener(v ->
                startActivity(new Intent(this, customermessage.class)));

        findViewById(R.id.navProfile).setOnClickListener(v -> { });
    }

    private void editProfileName() {

        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setText(tvProfileName.getText().toString());
        input.setPadding(35, 20, 35, 20);

        new AlertDialog.Builder(this)
                .setTitle("Edit Customer Name")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {

                    String newName = input.getText().toString().trim();

                    if (newName.isEmpty()) {
                        Toast.makeText(this, "Name cannot be empty",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    preferences.edit()
                            .putString("profileName", newName)
                            .apply();

                    tvProfileName.setText(newName);
                    tvProfileInitial.setText(
                            newName.substring(0, 1).toUpperCase()
                    );

                    Toast.makeText(this, "Name updated successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showProfileImage(Uri uri) {
        ivProfilePhoto.setImageURI(uri);
        ivProfilePhoto.setVisibility(ImageView.VISIBLE);
        tvProfileInitial.setVisibility(TextView.GONE);
    }
}
