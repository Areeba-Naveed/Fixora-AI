package com.fixora.ai;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class customermessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customermessage);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.chatAhmed).setOnClickListener(v ->
                Toast.makeText(this, "Ahmed chat selected", Toast.LENGTH_SHORT).show());

        findViewById(R.id.chatAli).setOnClickListener(v ->
                Toast.makeText(this, "Ali chat selected", Toast.LENGTH_SHORT).show());

        findViewById(R.id.chatUsman).setOnClickListener(v ->
                Toast.makeText(this, "Usman chat selected", Toast.LENGTH_SHORT).show());
    }
}
