package com.example.assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.adapter.OrderAdapter;
import com.example.assignment2.model.OrderModel;
import com.example.assignment2.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewFirebaseDataActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private TextView tvEmpty;
    private ProgressBar pbLoading;
    private Button btnRefresh;

    private List<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_firebase_data);

        Toolbar toolbar = findViewById(R.id.toolbar_fb_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Firebase Orders");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvOrders = findViewById(R.id.rv_fb_orders);
        tvEmpty = findViewById(R.id.tv_fb_empty);
        pbLoading = findViewById(R.id.pb_fb_view);
        btnRefresh = findViewById(R.id.btn_fb_refresh);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        btnRefresh.setOnClickListener(v -> fetchFirebaseData());

        fetchFirebaseData();
    }

    private void fetchFirebaseData() {
        pbLoading.setVisibility(View.VISIBLE);
        orderList.clear();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : "sample_uid";

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pbLoading.setVisibility(View.GONE);
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        OrderModel order = ds.getValue(OrderModel.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }
                }

                if (orderList.isEmpty()) {
                    // Fallback to local cart orders for display if Firebase nodes are empty
                    orderList.addAll(CartManager.getOrders(ViewFirebaseDataActivity.this));
                }

                tvEmpty.setVisibility(orderList.isEmpty() ? View.VISIBLE : View.GONE);
                rvOrders.setVisibility(orderList.isEmpty() ? View.GONE : View.VISIBLE);

                adapter = new OrderAdapter(ViewFirebaseDataActivity.this, orderList, null);
                rvOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(ViewFirebaseDataActivity.this, "Firebase Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                orderList.addAll(CartManager.getOrders(ViewFirebaseDataActivity.this));
                adapter = new OrderAdapter(ViewFirebaseDataActivity.this, orderList, null);
                rvOrders.setAdapter(adapter);
            }
        });
    }
}
