package com.fixora.ai;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AiDiagnosisClient {

    // Replace this IP with your laptop's IPv4 address
    private static final String API_URL =
            "http://192.168.100.139:3000/diagnose";

    public interface DiagnosisCallback {
        void onSuccess(JSONObject result);
        void onError(String message);
    }

    public static void diagnose(
            Context context,
            Uri imageUri,
            Bitmap imageBitmap,
            String description,
            DiagnosisCallback callback
    ) {

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        Handler mainHandler =
                new Handler(Looper.getMainLooper());

        Context appContext = context.getApplicationContext();

        executor.execute(() -> {

            HttpURLConnection connection = null;

            try {
                String boundary =
                        "----Fixora" + UUID.randomUUID().toString();

                URL url = new URL(API_URL);

                connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(90000);
                connection.setDoOutput(true);

                connection.setRequestProperty(
                        "Content-Type",
                        "multipart/form-data; boundary=" + boundary
                );

                DataOutputStream output =
                        new DataOutputStream(connection.getOutputStream());

                // Send text description
                if (description != null && !description.trim().isEmpty()) {

                    writeTextPart(
                            output,
                            boundary,
                            "description",
                            description
                    );
                }

                // Send gallery image
                if (imageUri != null) {

                    String mimeType =
                            appContext.getContentResolver()
                                    .getType(imageUri);

                    if (mimeType == null) {
                        mimeType = "image/jpeg";
                    }

                    if (!mimeType.equals("image/jpeg")
                            && !mimeType.equals("image/png")
                            && !mimeType.equals("image/webp")) {
                        throw new Exception(
                                "Please select a JPG, PNG or WEBP image."
                        );
                    }

                    String extension =
                            mimeType.equals("image/png") ? "png" :
                                    mimeType.equals("image/webp") ? "webp" : "jpg";

                    writeImageHeader(
                            output, boundary, mimeType, extension
                    );

                    InputStream imageStream =
                            appContext.getContentResolver()
                                    .openInputStream(imageUri);

                    if (imageStream == null) {
                        throw new Exception("Cannot read selected image.");
                    }

                    copyStream(imageStream, output);
                    imageStream.close();

                    output.writeBytes("\r\n");
                }

                // Send camera bitmap
                else if (imageBitmap != null) {

                    writeImageHeader(
                            output, boundary, "image/jpeg", "jpg"
                    );

                    ByteArrayOutputStream byteArray =
                            new ByteArrayOutputStream();

                    imageBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            85,
                            byteArray
                    );

                    output.write(byteArray.toByteArray());
                    output.writeBytes("\r\n");
                }

                output.writeBytes("--" + boundary + "--\r\n");

                output.flush();
                output.close();

                int status = connection.getResponseCode();

                InputStream responseStream =
                        status >= 200 && status < 300
                                ? connection.getInputStream()
                                : connection.getErrorStream();

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        responseStream,
                                        StandardCharsets.UTF_8
                                )
                        );

                StringBuilder responseText = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    responseText.append(line);
                }

                reader.close();

                if (status < 200 || status >= 300) {
                    JSONObject errorJson =
                            new JSONObject(responseText.toString());

                    throw new Exception(
                            errorJson.optString(
                                    "error",
                                    "Server error: " + status
                            )
                    );
                }

                JSONObject result =
                        new JSONObject(responseText.toString());

                mainHandler.post(
                        () -> callback.onSuccess(result)
                );

            } catch (Exception e) {

                String message = e.getMessage();

                mainHandler.post(
                        () -> callback.onError(
                                message == null
                                        ? "Something went wrong."
                                        : message
                        )
                );

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }

                executor.shutdown();
            }
        });
    }

    private static void writeTextPart(
            DataOutputStream output,
            String boundary,
            String name,
            String value
    ) throws Exception {

        output.writeBytes("--" + boundary + "\r\n");

        output.writeBytes(
                "Content-Disposition: form-data; name=\""
                        + name + "\"\r\n"
        );

        output.writeBytes(
                "Content-Type: text/plain; charset=UTF-8\r\n\r\n"
        );

        output.write(value.getBytes(StandardCharsets.UTF_8));
        output.writeBytes("\r\n");
    }

    private static void writeImageHeader(
            DataOutputStream output,
            String boundary,
            String mimeType,
            String extension
    ) throws Exception {

        output.writeBytes("--" + boundary + "\r\n");

        output.writeBytes(
                "Content-Disposition: form-data; name=\"image\"; filename=\"photo."
                        + extension + "\"\r\n"
        );

        output.writeBytes(
                "Content-Type: " + mimeType + "\r\n\r\n"
        );
    }

    private static void copyStream(
            InputStream input,
            DataOutputStream output
    ) throws Exception {

        byte[] buffer = new byte[8192];

        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
