package com.example.assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.db.DatabaseHelper;
import com.example.assignment2.model.Restaurant;

import java.util.List;

public class SqliteDatabaseActivity extends AppCompatActivity {

    private EditText etName, etCuisine, etLocation;
    private Button btnAdd, btnUpdate, btnDelete;
    private RecyclerView rvRestaurants;

    private DatabaseHelper dbHelper;
    private List<Restaurant> restaurantList;
    private Restaurant selectedRestaurant = null;
    private RestaurantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_database);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar_db);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Restaurant Database (SQLite)");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etName = findViewById(R.id.et_db_name);
        etCuisine = findViewById(R.id.et_db_cuisine);
        etLocation = findViewById(R.id.et_db_location);

        btnAdd = findViewById(R.id.btn_db_add);
        btnUpdate = findViewById(R.id.btn_db_update);
        btnDelete = findViewById(R.id.btn_db_delete);

        rvRestaurants = findViewById(R.id.rv_db_restaurants);
        rvRestaurants.setLayoutManager(new LinearLayoutManager(this));

        loadRestaurants();

        btnAdd.setOnClickListener(v -> addRestaurant());
        btnUpdate.setOnClickListener(v -> updateRestaurant());
        btnDelete.setOnClickListener(v -> deleteRestaurant());
    }

    private void loadRestaurants() {
        restaurantList = dbHelper.getAllRestaurants();
        adapter = new RestaurantAdapter();
        rvRestaurants.setAdapter(adapter);
    }

    private void addRestaurant() {
        String name = etName.getText().toString().trim();
        String cuisine = etCuisine.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || cuisine.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.addRestaurant(new Restaurant(name, cuisine, location));
        if (id > 0) {
            Toast.makeText(this, "Restaurant Added Successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadRestaurants();
        } else {
            Toast.makeText(this, "Failed to add restaurant", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRestaurant() {
        if (selectedRestaurant == null) {
            Toast.makeText(this, "Select a restaurant from list to update", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String cuisine = etCuisine.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || cuisine.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedRestaurant.setName(name);
        selectedRestaurant.setCuisine(cuisine);
        selectedRestaurant.setLocation(location);

        int rows = dbHelper.updateRestaurant(selectedRestaurant);
        if (rows > 0) {
            Toast.makeText(this, "Restaurant Updated Successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadRestaurants();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRestaurant() {
        if (selectedRestaurant == null) {
            Toast.makeText(this, "Select a restaurant from list to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete " + selectedRestaurant.getName() + "?")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    dbHelper.deleteRestaurant(selectedRestaurant.getId());
                    Toast.makeText(this, "Restaurant Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                    loadRestaurants();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void clearFields() {
        etName.setText("");
        etCuisine.setText("");
        etLocation.setText("");
        selectedRestaurant = null;
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.VH> {

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(SqliteDatabaseActivity.this).inflate(R.layout.item_restaurant, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Restaurant r = restaurantList.get(position);
            holder.tvId.setText(String.valueOf(r.getId()));
            holder.tvName.setText(r.getName());
            holder.tvCuisine.setText(r.getCuisine());
            holder.tvLocation.setText(r.getLocation());

            holder.itemView.setOnClickListener(v -> {
                selectedRestaurant = r;
                etName.setText(r.getName());
                etCuisine.setText(r.getCuisine());
                etLocation.setText(r.getLocation());
                Toast.makeText(SqliteDatabaseActivity.this, "Selected: " + r.getName(), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return restaurantList.size();
        }

        class VH extends RecyclerView.ViewHolder {
            TextView tvId, tvName, tvCuisine, tvLocation;

            public VH(@NonNull View itemView) {
                super(itemView);
                tvId = itemView.findViewById(R.id.tv_item_id);
                tvName = itemView.findViewById(R.id.tv_item_name);
                tvCuisine = itemView.findViewById(R.id.tv_item_cuisine);
                tvLocation = itemView.findViewById(R.id.tv_item_location);
            }
        }
    }
}
