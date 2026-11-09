
package com.fixora.ai;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HelpSupportActivity extends AppCompatActivity {

    private EditText etSearchHelp;

    private TextView[] questions;
    private TextView[] answers;

    private final String[] faqTitles = {
            "How can I book a service?",
            "How does AI Diagnosis work?",
            "How can I cancel a booking?",
            "Which payment methods are supported?"
    };

    // Replace these with your actual support contact details.
    private static final String SUPPORT_EMAIL = "areebanaveed754@gmail.com";
    private static final String SUPPORT_PHONE = "03286556204";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        etSearchHelp = findViewById(R.id.etSearchHelp);

        questions = new TextView[]{
                findViewById(R.id.faqBooking),
                findViewById(R.id.faqAI),
                findViewById(R.id.faqCancel),
                findViewById(R.id.faqPayment)
        };

        answers = new TextView[]{
                findViewById(R.id.answerBooking),
                findViewById(R.id.answerAI),
                findViewById(R.id.answerCancel),
                findViewById(R.id.answerPayment)
        };

        // Back button
        findViewById(R.id.btnHelpBack).setOnClickListener(v -> finish());

        // FAQ expand and collapse
        for (int i = 0; i < questions.length; i++) {
            final int position = i;

            questions[i].setOnClickListener(v -> {
                if (answers[position].getVisibility() == View.VISIBLE) {
                    answers[position].setVisibility(View.GONE);
                    questions[position].setText(faqTitles[position] + "     +");
                } else {
                    answers[position].setVisibility(View.VISIBLE);
                    questions[position].setText(faqTitles[position] + "     −");
                }
            });
        }

        // FAQ search
        etSearchHelp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String search = s.toString().trim().toLowerCase();

                for (int i = 0; i < questions.length; i++) {
                    boolean matches =
                            faqTitles[i].toLowerCase().contains(search);

                    questions[i].setVisibility(
                            matches ? View.VISIBLE : View.GONE
                    );

                    answers[i].setVisibility(View.GONE);
                    questions[i].setText(faqTitles[i] + "     +");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Email support
        findViewById(R.id.btnEmailSupport).setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Fixora AI - Support Request");

            try {
                startActivity(Intent.createChooser(
                        emailIntent, "Send Email"
                ));
            } catch (Exception e) {
                Toast.makeText(this,
                        "No email app found",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Call support
        findViewById(R.id.btnCallSupport).setOnClickListener(v -> {
            Intent callIntent = new Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + SUPPORT_PHONE)
            );

            startActivity(callIntent);
        });

        // Report a problem
        findViewById(R.id.btnReportProblem).setOnClickListener(v ->
                showReportDialog()
        );
    }

    private void showReportDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = dp(20);
        layout.setPadding(padding, padding, padding, padding);

        TextView categoryLabel = new TextView(this);
        categoryLabel.setText("Select Issue Category");
        categoryLabel.setTextSize(15);
        categoryLabel.setTextColor(0xFFFFFFFF);

        Spinner spinner = new Spinner(this);

        String[] categories = {
                "Booking",
                "Payment",
                "AI Diagnosis",
                "Account",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        EditText etSubject = new EditText(this);
        etSubject.setHint("Enter problem subject");
        etSubject.setSingleLine(true);

        EditText etDescription = new EditText(this);
        etDescription.setHint("Describe your problem");
        etDescription.setMinLines(3);
        etDescription.setGravity(
                android.view.Gravity.TOP
        );

        layout.addView(categoryLabel);
        layout.addView(spinner);
        layout.addView(etSubject);
        layout.addView(etDescription);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Report a Problem")
                .setView(layout)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Submit", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button submitButton = dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            );

            submitButton.setOnClickListener(v -> {

                String category = spinner.getSelectedItem().toString();
                String subject = etSubject.getText().toString().trim();
                String description =
                        etDescription.getText().toString().trim();

                if (subject.isEmpty()) {
                    etSubject.setError("Please enter a subject");
                    return;
                }

                if (description.isEmpty()) {
                    etDescription.setError(
                            "Please describe your problem"
                    );
                    return;
                }

                String report =
                        "Category: " + category +
                                "\nSubject: " + subject +
                                "\nDescription: " + description;

                getSharedPreferences(
                        "FixoraSupport",
                        MODE_PRIVATE
                ).edit()
                        .putString(
                                "lastReport",
                                report
                        )
                        .apply();

                Toast.makeText(
                        this,
                        "Your report has been saved successfully!",
                        Toast.LENGTH_LONG
                ).show();

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private int dp(int value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }
}
