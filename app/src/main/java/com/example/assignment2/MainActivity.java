package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.assignment2.utils.SettingsManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Spinner spRestaurant, spFood;
    private TextView tvLocation;
    private ImageView ivCarousel;
    private SettingsManager settingsManager;

    private final int[] carouselImages = {
            R.drawable.margherita_pizza,
            R.drawable.farmhouse_pizza,
            R.drawable.cheese_burger,
            R.drawable.white_sauce_pasta
    };
    private int currentCarouselIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsManager = new SettingsManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        // UI components
        spRestaurant = findViewById(R.id.sp_restaurant);
        spFood = findViewById(R.id.sp_food);
        tvLocation = findViewById(R.id.tv_location);
        ivCarousel = findViewById(R.id.iv_carousel);
        ImageView ivCart = findViewById(R.id.iv_cart);

        Button btnEditLocation = findViewById(R.id.btn_edit_location);
        Button btnMoreOptions = findViewById(R.id.btn_more_options);
        Button btnVisitWebsite = findViewById(R.id.btn_visit_website);
        ImageButton ibLeft = findViewById(R.id.ib_carousel_left);
        ImageButton ibRight = findViewById(R.id.ib_carousel_right);

        Button btnAddMargherita = findViewById(R.id.btn_add_margherita);
        Button btnAddFarmhouse = findViewById(R.id.btn_add_farmhouse);
        Button btnAddBurger = findViewById(R.id.btn_add_burger);
        Button btnAddPasta = findViewById(R.id.btn_add_pasta);

        // Spinners
        String[] restaurants = {"Domino's Pizza", "Pizza Hut", "Burger King", "Subway"};
        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, restaurants);
        spRestaurant.setAdapter(restaurantAdapter);

        String[] foods = {"Margherita Pizza", "Farmhouse Pizza", "Cheese Burger", "White Sauce Pasta"};
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, foods);
        spFood.setAdapter(foodAdapter);

        spFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCarouselIndex = position;
                ivCarousel.setImageResource(carouselImages[currentCarouselIndex]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Carousel buttons
        ibLeft.setOnClickListener(v -> {
            currentCarouselIndex = (currentCarouselIndex - 1 + carouselImages.length) % carouselImages.length;
            ivCarousel.setImageResource(carouselImages[currentCarouselIndex]);
            spFood.setSelection(currentCarouselIndex);
        });

        ibRight.setOnClickListener(v -> {
            currentCarouselIndex = (currentCarouselIndex + 1) % carouselImages.length;
            ivCarousel.setImageResource(carouselImages[currentCarouselIndex]);
            spFood.setSelection(currentCarouselIndex);
        });

        // Location & Edit
        tvLocation.setText(settingsManager.getRestaurantLocation());
        btnEditLocation.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EditLocationActivity.class)));

        // Add buttons
        btnAddMargherita.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Added Margherita Pizza (₹249) to Cart!", Toast.LENGTH_SHORT).show());
        btnAddFarmhouse.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Added Farmhouse Pizza (₹249) to Cart!", Toast.LENGTH_SHORT).show());
        btnAddBurger.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Added Cheese Burger (₹199) to Cart!", Toast.LENGTH_SHORT).show());
        btnAddPasta.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Added White Sauce Pasta (₹299) to Cart!", Toast.LENGTH_SHORT).show());

        // Cart Icon
        ivCart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyOrdersActivity.class)));

        // Bottom Bar Buttons
        btnMoreOptions.setOnClickListener(this::showMoreOptionsPopupMenu);
        btnVisitWebsite.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RestaurantWebsiteActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tvLocation != null && settingsManager != null) {
            tvLocation.setText(settingsManager.getRestaurantLocation());
        }
        updateNavHeader();
    }

    private void updateNavHeader() {
        if (navigationView == null) return;
        View headerView = navigationView.getHeaderView(0);
        if (headerView == null) return;

        TextView tvName = headerView.findViewById(R.id.nav_header_name);
        TextView tvEmail = headerView.findViewById(R.id.nav_header_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            tvName.setText(name != null && !name.isEmpty() ? name : "Hitarth Shah");
            tvEmail.setText(email != null ? email : "hitarth@example.com");
        } else {
            tvName.setText("Hitarth Shah");
            tvEmail.setText("hitarth@example.com");
        }
    }

    private void showMoreOptionsPopupMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenu().add("Add to Cart");
        popup.getMenu().add("Buy Now");
        popup.getMenu().add("Add to Wishlist");
        popup.getMenu().add("View Restaurant Details");
        popup.getMenu().add("Save to File (TXT/CSV)");
        popup.getMenu().add("View on Map");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Add to Cart":
                case "Buy Now":
                    startActivity(new Intent(MainActivity.this, MyOrdersActivity.class));
                    return true;
                case "Add to Wishlist":
                    Toast.makeText(MainActivity.this, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                    return true;
                case "View Restaurant Details":
                    startActivity(new Intent(MainActivity.this, ShareRestaurantActivity.class));
                    return true;
                case "Save to File (TXT/CSV)":
                    startActivity(new Intent(MainActivity.this, FileHandlingTxtActivity.class));
                    return true;
                case "View on Map":
                    startActivity(new Intent(MainActivity.this, MapLocationActivity.class));
                    return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Already on Home
        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(this, MyOrdersActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_db) {
            startActivity(new Intent(this, SqliteDatabaseActivity.class));
        } else if (id == R.id.nav_file) {
            startActivity(new Intent(this, FileHandlingTxtActivity.class));
        } else if (id == R.id.nav_location) {
            startActivity(new Intent(this, LocationTrackerActivity.class));
        } else if (id == R.id.nav_firebase) {
            startActivity(new Intent(this, ViewFirebaseDataActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_logout) {
            showLogoutDialog();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("LOGOUT", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
