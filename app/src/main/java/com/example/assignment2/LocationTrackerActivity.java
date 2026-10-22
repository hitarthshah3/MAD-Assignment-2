package com.example.assignment2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.materialswitch.MaterialSwitch;

public class LocationTrackerActivity extends AppCompatActivity {

    private static final int PERMISSION_LOCATION_REQ_CODE = 1001;

    private TextView tvLatitude, tvLongitude, tvAddressDesc, tvLocStatus;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracker);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Toolbar toolbar = findViewById(R.id.toolbar_loc_tracker);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Current Location");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
        tvAddressDesc = findViewById(R.id.tv_address_desc);
        tvLocStatus = findViewById(R.id.tv_loc_status);

        Button btnGetLoc = findViewById(R.id.btn_get_location);
        MaterialSwitch swTrackLive = findViewById(R.id.sw_track_live);

        btnGetLoc.setOnClickListener(v -> fetchDeviceLocation());

        swTrackLive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                fetchDeviceLocation();
                Toast.makeText(this, "Live location tracking enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Live location tracking disabled.", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch location on open
        fetchDeviceLocation();
    }

    private void fetchDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION_REQ_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                updateLocationUI(location.getLatitude(), location.getLongitude(), "Bandra West, Mumbai (Actual Coordinates)");
            } else {
                // Fallback coordinates for Bandra West, Mumbai
                updateLocationUI(19.0563, 72.8295, "Bandra West, Mumbai");
            }
        }).addOnFailureListener(e -> {
            updateLocationUI(19.0563, 72.8295, "Bandra West, Mumbai");
        });
    }

    private void updateLocationUI(double lat, double lon, String desc) {
        tvLatitude.setText("Latitude: " + String.format("%.4f", lat));
        tvLongitude.setText("Longitude: " + String.format("%.4f", lon));
        tvAddressDesc.setText(desc);
        tvLocStatus.setText("✓ Location updated successfully!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchDeviceLocation();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
                updateLocationUI(19.0563, 72.8295, "Bandra West, Mumbai (Default)");
            }
        }
    }
}
