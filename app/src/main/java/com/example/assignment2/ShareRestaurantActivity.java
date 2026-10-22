package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ShareRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_restaurant);

        Toolbar toolbar = findViewById(R.id.toolbar_share);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Share Restaurant");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        Button btnShare = findViewById(R.id.btn_share_now);
        btnShare.setOnClickListener(v -> shareDetails());
    }

    private void shareDetails() {
        String shareBody = "Check out Domino's Pizza at Bandra West, Mumbai on Foodie Order! Order delicious Italian food now.";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Foodie Order - Domino's Pizza");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
