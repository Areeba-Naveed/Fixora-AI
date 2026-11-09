
package com.fixora.ai;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class notificationsactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Back button
        findViewById(R.id.btnNotificationBack).setOnClickListener(v -> {
            finish();
        });
    }
}
