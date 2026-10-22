package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.adapter.OrderAdapter;
import com.example.assignment2.model.OrderModel;
import com.example.assignment2.utils.CartManager;

import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private TextView tvEmptyOrders, tvTotalOrders, tvTotalAmount;
    private OrderAdapter adapter;
    private List<OrderModel> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Toolbar toolbar = findViewById(R.id.toolbar_orders);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Orders");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvOrders = findViewById(R.id.rv_orders);
        tvEmptyOrders = findViewById(R.id.tv_empty_orders);
        tvTotalOrders = findViewById(R.id.tv_total_orders);
        tvTotalAmount = findViewById(R.id.tv_total_amount);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        orderList = CartManager.getOrders(this);

        if (orderList.isEmpty()) {
            tvEmptyOrders.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.GONE);
        } else {
            tvEmptyOrders.setVisibility(View.GONE);
            rvOrders.setVisibility(View.VISIBLE);
        }

        adapter = new OrderAdapter(this, orderList, this::showOrderOptionsDialog);
        rvOrders.setAdapter(adapter);

        updateSummary();
    }

    private void updateSummary() {
        int count = orderList != null ? orderList.size() : 0;
        double total = CartManager.getTotalAmount(this);

        tvTotalOrders.setText("Total Orders: " + count);
        tvTotalAmount.setText("Total Amount: ₹" + (int) total);
    }

    private void showOrderOptionsDialog(OrderModel order) {
        View anchor = rvOrders.findViewHolderForAdapterPosition(orderList.indexOf(order)).itemView;
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenu().add("Reorder");
        popup.getMenu().add("View Details");
        popup.getMenu().add("Cancel Order");
        popup.getMenu().add("Share Order");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Reorder":
                    CartManager.addOrder(this, order.getRestaurantName(), order.getItemName(), order.getPrice(), order.getImageResId());
                    Toast.makeText(this, "Reordered " + order.getItemName() + "!", Toast.LENGTH_SHORT).show();
                    loadOrders();
                    return true;
                case "View Details":
                    showOrderDetailsDialog(order);
                    return true;
                case "Cancel Order":
                    handleCancelOrder(order);
                    return true;
                case "Share Order":
                    shareOrderDetails(order);
                    return true;
            }
            return false;
        });
        popup.show();
    }

    private void showOrderDetailsDialog(OrderModel order) {
        String details = "Order ID: " + order.getOrderId() + "\n"
                + "Restaurant: " + order.getRestaurantName() + "\n"
                + "Item: " + order.getItemName() + "\n"
                + "Price: ₹" + (int) order.getPrice() + "\n"
                + "Date: " + order.getDate() + "\n"
                + "Status: " + order.getStatus();

        new AlertDialog.Builder(this)
                .setTitle("Order Details")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }

    private void handleCancelOrder(OrderModel order) {
        if ("Delivered".equalsIgnoreCase(order.getStatus())) {
            Toast.makeText(this, "Delivered orders cannot be cancelled.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel " + order.getItemName() + "?")
                .setPositiveButton("YES", (dialog, which) -> {
                    CartManager.cancelOrder(this, order.getOrderId());
                    Toast.makeText(this, "Order cancelled.", Toast.LENGTH_SHORT).show();
                    loadOrders();
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void shareOrderDetails(OrderModel order) {
        String text = "I ordered " + order.getItemName() + " from " + order.getRestaurantName()
                + " for ₹" + (int) order.getPrice() + " using Foodie Order!";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(shareIntent, "Share Order via"));
    }
}
