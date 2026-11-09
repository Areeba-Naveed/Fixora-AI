
package com.fixora.ai;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentMethodsActivity extends AppCompatActivity {

    LinearLayout cardSavedMethod, emptySavedMethods;
    TextView tvSavedMethod, tvSavedDetails;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        prefs = getSharedPreferences("FixoraPayments", MODE_PRIVATE);

        cardSavedMethod = findViewById(R.id.cardSavedMethod);
        emptySavedMethods = findViewById(R.id.emptySavedMethods);

        tvSavedMethod = findViewById(R.id.tvSavedMethod);
        tvSavedDetails = findViewById(R.id.tvSavedDetails);

        findViewById(R.id.btnPaymentBack).setOnClickListener(v -> finish());

        findViewById(R.id.optionAddCard).setOnClickListener(v -> showCardDialog());

        findViewById(R.id.optionJazzCash).setOnClickListener(v ->
                showWalletDialog("JazzCash"));

        findViewById(R.id.optionEasypaisa).setOnClickListener(v ->
                showWalletDialog("Easypaisa"));

        findViewById(R.id.optionCash).setOnClickListener(v ->
                savePayment("Cash on Service", "Pay after repair completion"));

        findViewById(R.id.btnRemoveCard).setOnClickListener(v ->
                removePayment());

        findViewById(R.id.optionPaymentHistory).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Payment History")
                        .setMessage("No payment transactions yet.")
                        .setPositiveButton("OK", null)
                        .show());

        loadSavedPayment();
    }

    // Add Debit / Credit Card
    private void showCardDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 10, 24, 5);

        EditText name = createField("Cardholder Name",
                InputType.TYPE_CLASS_TEXT);

        EditText number = createField("Card Number",
                InputType.TYPE_CLASS_NUMBER);

        number.setFilters(new android.text.InputFilter[]{
                new android.text.InputFilter.LengthFilter(19)
        });

        EditText expiry = createField("Expiry Date (MM/YY)",
                InputType.TYPE_CLASS_DATETIME);

        expiry.setFilters(new android.text.InputFilter[]{
                new android.text.InputFilter.LengthFilter(5)
        });

        EditText cvv = createField("CVV",
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        cvv.setFilters(new android.text.InputFilter[]{
                new android.text.InputFilter.LengthFilter(4)
        });

        layout.addView(name);
        layout.addView(number);
        layout.addView(expiry);
        layout.addView(cvv);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Card")
                .setView(layout)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save Card", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String cardName = name.getText().toString().trim();
                        String cardNumber = number.getText().toString().replace(" ", "");
                        String exp = expiry.getText().toString().trim();
                        String securityCode = cvv.getText().toString().trim();

                        if (cardName.isEmpty() ||
                                cardNumber.length() < 12 ||
                                exp.length() != 5 ||
                                securityCode.length() < 3) {

                            Toast.makeText(this,
                                    "Please enter valid demo card details",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String lastFour = cardNumber.substring(
                                cardNumber.length() - 4);

                        // Only display information is saved.
                        // Full card number and CVV are NOT stored.
                        prefs.edit()
                                .putString("paymentMethod", "Debit / Credit Card")
                                .putString("paymentDetails", "•••• " + lastFour)
                                .putBoolean("hasPayment", true)
                                .apply();

                        loadSavedPayment();

                        dialog.dismiss();

                        Toast.makeText(this,
                                "Demo card added successfully",
                                Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.show();
    }

    // JazzCash / Easypaisa
    private void showWalletDialog(String wallet) {

        EditText phone = createField("Enter mobile account number",
                InputType.TYPE_CLASS_PHONE);

        new AlertDialog.Builder(this)
                .setTitle(wallet)
                .setMessage("Enter your " + wallet + " account number.")
                .setView(phone)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Continue", (dialog, which) -> {

                    String mobile = phone.getText().toString().trim();

                    if (mobile.length() < 10) {
                        Toast.makeText(this,
                                "Please enter a valid mobile number",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Demo selection only, not a real wallet connection.
                    savePayment(wallet, "Mobile: " +
                            mobile.substring(mobile.length() - 4));
                })
                .show();
    }

    // Save selected payment method
    private void savePayment(String method, String details) {

        prefs.edit()
                .putString("paymentMethod", method)
                .putString("paymentDetails", details)
                .putBoolean("hasPayment", true)
                .apply();

        loadSavedPayment();

        Toast.makeText(this,
                method + " selected successfully",
                Toast.LENGTH_SHORT).show();
    }

    // Display saved payment method
    private void loadSavedPayment() {

        boolean hasPayment = prefs.getBoolean("hasPayment", false);

        if (hasPayment) {

            cardSavedMethod.setVisibility(View.VISIBLE);
            emptySavedMethods.setVisibility(View.GONE);

            tvSavedMethod.setText(
                    prefs.getString("paymentMethod", ""));

            tvSavedDetails.setText(
                    prefs.getString("paymentDetails", ""));

        } else {

            cardSavedMethod.setVisibility(View.GONE);
            emptySavedMethods.setVisibility(View.VISIBLE);
        }
    }

    // Remove saved payment method
    private void removePayment() {

        new AlertDialog.Builder(this)
                .setTitle("Remove Payment Method")
                .setMessage("Are you sure you want to remove this payment method?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Remove", (dialog, which) -> {

                    prefs.edit()
                            .remove("paymentMethod")
                            .remove("paymentDetails")
                            .putBoolean("hasPayment", false)
                            .apply();

                    loadSavedPayment();

                    Toast.makeText(this,
                            "Payment method removed",
                            Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // Reusable input field
    private EditText createField(String hint, int inputType) {

        EditText editText = new EditText(this);

        editText.setHint(hint);
        editText.setInputType(inputType);
        editText.setTextSize(14);
        editText.setSingleLine(true);
        editText.setTextColor(Color.BLACK);
        editText.setHintTextColor(Color.GRAY);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        52);

        params.bottomMargin = 8;
        editText.setLayoutParams(params);

        return editText;
    }
}
