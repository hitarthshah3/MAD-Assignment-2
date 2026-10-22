package com.example.assignment2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail;
    private EditText etName, etPhone, etAddress, etCuisine;
    private Button btnSaveProfile;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("UserProfilePref", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvProfileName = findViewById(R.id.tv_profile_name);
        tvProfileEmail = findViewById(R.id.tv_profile_email);
        etName = findViewById(R.id.et_profile_name);
        etPhone = findViewById(R.id.et_profile_phone);
        etAddress = findViewById(R.id.et_profile_address);
        etCuisine = findViewById(R.id.et_profile_cuisine);
        btnSaveProfile = findViewById(R.id.btn_save_profile);

        loadProfileData();

        btnSaveProfile.setOnClickListener(v -> saveProfileData());
    }

    private void loadProfileData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String defaultName = "Hitarth Shah";
        String defaultEmail = "hitarth@example.com";

        if (user != null) {
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                defaultName = user.getDisplayName();
            }
            if (user.getEmail() != null) {
                defaultEmail = user.getEmail();
            }
        }

        String name = prefs.getString("name", defaultName);
        String phone = prefs.getString("phone", "+91 9876543210");
        String address = prefs.getString("address", "Bandra West, Mumbai");
        String cuisine = prefs.getString("cuisine", "Italian");

        tvProfileName.setText(name);
        tvProfileEmail.setText(defaultEmail);
        etName.setText(name);
        etPhone.setText(phone);
        etAddress.setText(address);
        etCuisine.setText(cuisine);
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String cuisine = etCuisine.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name required");
            return;
        }

        prefs.edit()
                .putString("name", name)
                .putString("phone", phone)
                .putString("address", address)
                .putString("cuisine", cuisine)
                .apply();

        tvProfileName.setText(name);
        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
