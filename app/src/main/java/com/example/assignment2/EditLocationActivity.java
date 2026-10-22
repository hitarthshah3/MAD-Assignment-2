package com.example.assignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.assignment2.utils.SettingsManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText etLocation;
    private GoogleMap mMap;
    private SettingsManager settingsManager;
    private static final LatLng DEFAULT_LOC = new LatLng(19.0563, 72.8295);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        settingsManager = new SettingsManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_loc);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Location");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etLocation = findViewById(R.id.et_edit_location);
        etLocation.setText(settingsManager.getRestaurantLocation());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.edit_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnGetCurrLoc = findViewById(R.id.btn_get_curr_loc);
        Button btnCancel = findViewById(R.id.btn_cancel_loc);
        Button btnSave = findViewById(R.id.btn_save_loc);

        btnGetCurrLoc.setOnClickListener(v -> {
            etLocation.setText("Bandra West, Mumbai (GPS Current)");
            Toast.makeText(this, "Fetched Current Location!", Toast.LENGTH_SHORT).show();
        });

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String newLoc = etLocation.getText().toString().trim();
            if (!newLoc.isEmpty()) {
                settingsManager.setRestaurantLocation(newLoc);
                Toast.makeText(this, "Location saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                etLocation.setError("Location cannot be empty");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(DEFAULT_LOC).title("Restaurant Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOC, 14f));
    }
}
