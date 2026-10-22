package com.example.assignment2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.assignment2.R;
import com.example.assignment2.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartManager {

    private static final String PREF_NAME = "FoodieCartPref";
    private static List<OrderModel> orderList = null;

    public static List<OrderModel> getOrders(Context context) {
        if (orderList == null) {
            orderList = new ArrayList<>();
            seedDefaultOrders();
        }
        return orderList;
    }

    private static void seedDefaultOrders() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : "sample_uid";

        orderList.add(new OrderModel("ORD123", "Domino's Pizza", "12 May 2024", "Margherita Pizza", 249.0, "Delivered", R.drawable.margherita_pizza, uid));
        orderList.add(new OrderModel("ORD124", "Burger King", "10 May 2024", "Cheese Burger", 199.0, "Delivered", R.drawable.cheese_burger, uid));
        orderList.add(new OrderModel("ORD125", "Ovenstory Pizza", "08 May 2024", "White Sauce Pasta", 299.0, "Delivered", R.drawable.white_sauce_pasta, uid));
    }

    public static void addOrder(Context context, String restaurantName, String itemName, double price, int imageResId) {
        List<OrderModel> orders = getOrders(context);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : "sample_uid";

        String orderId = "ORD" + (100 + orders.size() + 1);
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());

        OrderModel newOrder = new OrderModel(orderId, restaurantName, currentDate, itemName, price, "Processing", imageResId, uid);
        orders.add(0, newOrder);
    }

    public static boolean cancelOrder(Context context, String orderId) {
        List<OrderModel> orders = getOrders(context);
        for (OrderModel order : orders) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                if ("Delivered".equalsIgnoreCase(order.getStatus())) {
                    return false; // Cannot cancel delivered orders
                }
                order.setStatus("Cancelled");
                return true;
            }
        }
        return false;
    }

    public static double getTotalAmount(Context context) {
        double total = 0;
        for (OrderModel order : getOrders(context)) {
            if (!"Cancelled".equalsIgnoreCase(order.getStatus())) {
                total += order.getPrice();
            }
        }
        return total;
    }
}
