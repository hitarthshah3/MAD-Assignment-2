package com.example.assignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.assignment2.R;
import com.example.assignment2.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FirebaseRealtimeDbActivity extends AppCompatActivity {

    private EditText etOrderId, etRestaurant, etItem, etPrice, etStatus;
    private TextView tvDbStatus;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_realtime_db);

        Toolbar toolbar = findViewById(R.id.toolbar_fb_db);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sync with Firebase");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etOrderId = findViewById(R.id.et_fb_order_id);
        etRestaurant = findViewById(R.id.et_fb_restaurant);
        etItem = findViewById(R.id.et_fb_item);
        etPrice = findViewById(R.id.et_fb_price);
        etStatus = findViewById(R.id.et_fb_status);
        tvDbStatus = findViewById(R.id.tv_fb_db_status);
        btnSave = findViewById(R.id.btn_save_to_firebase);

        btnSave.setOnClickListener(v -> saveToFirebase());
    }

    private void saveToFirebase() {
        String orderId = etOrderId.getText().toString().trim();
        String restaurant = etRestaurant.getText().toString().trim();
        String item = etItem.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String status = etStatus.getText().toString().trim();

        if (orderId.isEmpty() || restaurant.isEmpty() || item.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : "guest_user";
        String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());

        OrderModel order = new OrderModel(orderId, restaurant, date, item, price, status, R.drawable.margherita_pizza, uid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders").child(uid).child(orderId);
        ref.setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tvDbStatus.setText("✓ Data saved to Firebase Realtime Database!");
                Toast.makeText(FirebaseRealtimeDbActivity.this, "Order saved to Firebase!", Toast.LENGTH_SHORT).show();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Error saving to Firebase";
                tvDbStatus.setText("Failed: " + error);
                Toast.makeText(FirebaseRealtimeDbActivity.this, "Failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
