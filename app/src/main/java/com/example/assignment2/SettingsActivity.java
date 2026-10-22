package com.example.assignment2;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.assignment2.utils.SettingsManager;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        MaterialSwitch swNotifications = findViewById(R.id.sw_notifications);
        MaterialSwitch swLocation = findViewById(R.id.sw_location_services);
        MaterialSwitch swFirebase = findViewById(R.id.sw_firebase_sync);
        MaterialSwitch swBackup = findViewById(R.id.sw_auto_backup);
        MaterialSwitch swDark = findViewById(R.id.sw_dark_mode);

        TextView tvAbout = findViewById(R.id.tv_about_app);
        TextView tvHelp = findViewById(R.id.tv_help_feedback);

        swNotifications.setChecked(settingsManager.isNotificationsEnabled());
        swLocation.setChecked(settingsManager.isLocationServicesEnabled());
        swFirebase.setChecked(settingsManager.isFirebaseSyncEnabled());
        swBackup.setChecked(settingsManager.isAutoBackupEnabled());
        swDark.setChecked(settingsManager.isDarkModeEnabled());

        swNotifications.setOnCheckedChangeListener((v, isChecked) -> {
            settingsManager.setNotificationsEnabled(isChecked);
            Toast.makeText(this, "Notifications " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        swLocation.setOnCheckedChangeListener((v, isChecked) -> {
            settingsManager.setLocationServicesEnabled(isChecked);
            Toast.makeText(this, "Location Services " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        swFirebase.setOnCheckedChangeListener((v, isChecked) -> {
            settingsManager.setFirebaseSyncEnabled(isChecked);
            Toast.makeText(this, "Firebase Sync " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        swBackup.setOnCheckedChangeListener((v, isChecked) -> {
            settingsManager.setAutoBackupEnabled(isChecked);
            Toast.makeText(this, "Auto Backup " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        swDark.setOnCheckedChangeListener((v, isChecked) -> {
            settingsManager.setDarkModeEnabled(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        tvAbout.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("About Foodie Order")
                .setMessage("Foodie Order App v1.0\nCreated for Android Application Development Assignment.\nSupports Firebase Auth, Realtime DB, SQLite, Maps & Location Tracking.")
                .setPositiveButton("OK", null)
                .show());

        tvHelp.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Help & Feedback")
                .setMessage("For support or feedback, contact support@foodieorder.com or visit our website.")
                .setPositiveButton("OK", null)
                .show());
    }
}
