package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FirebaseAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);

        Toolbar toolbar = findViewById(R.id.toolbar_fb_auth);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Firebase Authentication");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        Button btnEmail = findViewById(R.id.btn_fb_email_login);
        Button btnGoogle = findViewById(R.id.btn_fb_google_login);

        btnEmail.setOnClickListener(v -> startActivity(new Intent(FirebaseAuthActivity.this, LoginActivity.class)));
        btnGoogle.setOnClickListener(v -> startActivity(new Intent(FirebaseAuthActivity.this, LoginActivity.class)));
    }
}
