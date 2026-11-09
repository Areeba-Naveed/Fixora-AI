
package com.fixora.ai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchBookingNotifications;
    private Switch switchPromotionalNotifications;

    private SharedPreferences settingsPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPrefs = getSharedPreferences(
                "FixoraSettings", MODE_PRIVATE
        );

        // Find views
        switchBookingNotifications =
                findViewById(R.id.switchBookingNotifications);

        switchPromotionalNotifications =
                findViewById(R.id.switchPromotionalNotifications);

        // Load saved switch settings
        switchBookingNotifications.setChecked(
                settingsPrefs.getBoolean("bookingNotifications", true)
        );

        switchPromotionalNotifications.setChecked(
                settingsPrefs.getBoolean("promotionalNotifications", false)
        );

        // Back button
        findViewById(R.id.btnSettingsBack).setOnClickListener(v ->
                finish()
        );

        // Save booking notification preference
        switchBookingNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        settingsPrefs.edit()
                                .putBoolean("bookingNotifications", isChecked)
                                .apply()
        );

        // Save promotional notification preference
        switchPromotionalNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        settingsPrefs.edit()
                                .putBoolean("promotionalNotifications", isChecked)
                                .apply()
        );

        // Edit Profile
        findViewById(R.id.settingEditProfile).setOnClickListener(v ->
                Toast.makeText(this,
                        "Open Edit Profile from your Profile screen",
                        Toast.LENGTH_SHORT).show()
        );

        // Change Password
        findViewById(R.id.settingChangePassword).setOnClickListener(v ->
                showInfoDialog(
                        "Change Password",
                        "Password change functionality will be connected with account authentication."
                )
        );

        // Privacy Policy
        findViewById(R.id.settingPrivacy).setOnClickListener(v ->
                showInfoDialog(
                        "Privacy Policy",
                        "Fixora AI uses account and service information to provide home maintenance services. Your information should be handled securely."
                )
        );

        // Terms and Conditions
        findViewById(R.id.settingTerms).setOnClickListener(v ->
                showInfoDialog(
                        "Terms & Conditions",
                        "By using Fixora AI, you agree to use the application responsibly and provide accurate booking information."
                )
        );

        // About Fixora AI
        findViewById(R.id.settingAbout).setOnClickListener(v ->
                showInfoDialog(
                        "About Fixora AI",
                        "Fixora AI is a home maintenance application that helps users identify repair issues using AI and connect with service providers."
                                + "\n\nVersion 1.0"
                )
        );

        // Logout
        findViewById(R.id.btnLogout).setOnClickListener(v ->
                showLogoutDialog()
        );
    }

    private void showInfoDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.dismiss()
                )
                .setPositiveButton("Logout", (dialog, which) -> {

                    // Clear login session only.
                    // Keep saved profile information.
                    getSharedPreferences(
                            "FixoraLogin", MODE_PRIVATE
                    ).edit().clear().apply();

                    // Open Login screen
                    Intent intent = new Intent();
                    intent.setClassName(
                            this,
                            "com.fixora.ai.loginscreen"
                    );

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    try {
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(
                                this,
                                "Login screen class name check karein",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                })
                .show();
    }
}
