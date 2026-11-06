package com.fixora.ai;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class aidiagnosisactivity extends AppCompatActivity {

    private TextView tabUpload, tabDescribe, btnSubmit, tvPhotoTitle;

    private View panelUpload, panelDescribe, llPlaceholder;

    private ImageView ivPhoto;

    private EditText etDescribe;

    private boolean uploadMode = true;

    private boolean hasPhoto = false;

    private Uri photoUri = null;

    private Bitmap photoBitmap = null;

    public static Uri sharedUri = null;

    public static Bitmap sharedBitmap = null;


    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            photoUri = uri;
                            photoBitmap = null;

                            ivPhoto.setImageURI(uri);

                            onPhotoSelected();
                        }
                    }
            );


    private final ActivityResultLauncher<Void> takePhoto =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {
                        if (bitmap != null) {
                            photoBitmap = bitmap;
                            photoUri = null;

                            ivPhoto.setImageBitmap(bitmap);

                            onPhotoSelected();
                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.aidiagnosisactivity);

        tabUpload = findViewById(R.id.tabUpload);

        tabDescribe = findViewById(R.id.tabDescribe);

        btnSubmit = findViewById(R.id.btnSubmit);

        tvPhotoTitle = findViewById(R.id.tvPhotoTitle);

        panelUpload = findViewById(R.id.panelUpload);

        panelDescribe = findViewById(R.id.panelDescribe);

        llPlaceholder = findViewById(R.id.llPlaceholder);

        ivPhoto = findViewById(R.id.ivPhoto);

        etDescribe = findViewById(R.id.etDescribe);


        findViewById(R.id.btnBack)
                .setOnClickListener(v -> finish());


        tabUpload.setOnClickListener(v -> setTab(true));

        tabDescribe.setOnClickListener(v -> setTab(false));


        View.OnClickListener openGallery =
                v -> pickImage.launch("image/*");


        findViewById(R.id.photoBox)
                .setOnClickListener(openGallery);

        findViewById(R.id.btnGallery)
                .setOnClickListener(openGallery);


        findViewById(R.id.btnCamera).setOnClickListener(v -> {

            try {
                takePhoto.launch(null);

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "Camera not available",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        btnSubmit.setOnClickListener(v -> submit());

        setTab(true);
    }


    private void onPhotoSelected() {

        hasPhoto = true;

        llPlaceholder.setVisibility(View.GONE);

        ivPhoto.setVisibility(View.VISIBLE);

        tvPhotoTitle.setText("Photo selected. Tap to change.");
    }


    private void setTab(boolean upload) {

        uploadMode = upload;

        panelUpload.setVisibility(
                upload ? View.VISIBLE : View.GONE
        );

        panelDescribe.setVisibility(
                upload ? View.GONE : View.VISIBLE
        );

        tabUpload.setBackgroundResource(
                upload
                        ? R.drawable.bg_tab_active
                        : R.drawable.bg_tab_inactive
        );

        tabUpload.setTextColor(
                upload ? 0xFFFFFFFF : 0xFF6B6890
        );

        tabDescribe.setBackgroundResource(
                upload
                        ? R.drawable.bg_tab_inactive
                        : R.drawable.bg_tab_active
        );

        tabDescribe.setTextColor(
                upload ? 0xFF6B6890 : 0xFFFFFFFF
        );

        btnSubmit.setText(
                upload ? "Upload Photo" : "Analyze Issue"
        );
    }


    private void submit() {

        String text = etDescribe.getText()
                .toString()
                .trim();

        if (uploadMode && !hasPhoto) {

            Toast.makeText(
                    this,
                    "Please select a photo first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!uploadMode && text.length() < 5) {

            Toast.makeText(
                    this,
                    "Please describe your problem",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Save current data before starting request
        final boolean sendingPhoto = uploadMode;

        final Uri selectedUri = sendingPhoto ? photoUri : null;

        final Bitmap selectedBitmap =
                sendingPhoto ? photoBitmap : null;

        final String description =
                sendingPhoto ? "" : text;


        btnSubmit.setEnabled(false);

        btnSubmit.setText("Analyzing...");

        btnSubmit.setAlpha(0.7f);


        AiDiagnosisClient.diagnose(
                this,
                selectedUri,
                selectedBitmap,
                description,

                new AiDiagnosisClient.DiagnosisCallback() {

                    @Override
                    public void onSuccess(org.json.JSONObject result) {

                        btnSubmit.setEnabled(true);

                        btnSubmit.setText(
                                sendingPhoto
                                        ? "Upload Photo"
                                        : "Analyze Issue"
                        );

                        btnSubmit.setAlpha(1f);


                        sharedUri = selectedUri;

                        sharedBitmap = selectedBitmap;


                        Intent intent = new Intent(
                                aidiagnosisactivity.this,
                                airesultactivity.class
                        );

                        intent.putExtra(
                                "diagnosis_json",
                                result.toString()
                        );

                        if (selectedUri != null) {

                            intent.putExtra(
                                    "image_uri",
                                    selectedUri.toString()
                            );
                        }

                        startActivity(intent);
                    }


                    @Override
                    public void onError(String message) {

                        btnSubmit.setEnabled(true);

                        btnSubmit.setText(
                                sendingPhoto
                                        ? "Upload Photo"
                                        : "Analyze Issue"
                        );

                        btnSubmit.setAlpha(1f);

                        Toast.makeText(
                                aidiagnosisactivity.this,
                                message,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}