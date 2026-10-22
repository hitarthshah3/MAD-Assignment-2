package com.example.assignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng RESTAURANT_LOC = new LatLng(19.0563, 72.8295); // Bandra West, Mumbai
    private static final LatLng USER_LOC = new LatLng(19.0600, 72.8350);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        Toolbar toolbar = findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Track Restaurant Location");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnShowRoute = findViewById(R.id.btn_show_route);
        btnShowRoute.setOnClickListener(v -> {
            Toast.makeText(MapLocationActivity.this, "Calculating route to Domino's Pizza (Bandra West)...", Toast.LENGTH_SHORT).show();
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(RESTAURANT_LOC, 15f));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Restaurant Marker
        mMap.addMarker(new MarkerOptions()
                .position(RESTAURANT_LOC)
                .title("Domino's Pizza")
                .snippet("Bandra West, Mumbai")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // User Location Marker
        mMap.addMarker(new MarkerOptions()
                .position(USER_LOC)
                .title("Your Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(RESTAURANT_LOC, 14f));
    }
}
