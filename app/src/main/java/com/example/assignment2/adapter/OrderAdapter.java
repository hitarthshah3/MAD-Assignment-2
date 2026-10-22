package com.example.assignment2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.model.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(OrderModel order);
    }

    private final Context context;
    private final List<OrderModel> orderList;
    private final OnOrderClickListener listener;

    public OrderAdapter(Context context, List<OrderModel> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.tvItemName.setText(order.getItemName());
        holder.tvRestaurant.setText(order.getRestaurantName());
        holder.tvPrice.setText("₹" + (int) order.getPrice());
        holder.tvDate.setText(order.getDate());
        holder.tvStatus.setText(order.getStatus());

        if (order.getImageResId() != 0) {
            holder.ivFood.setImageResource(order.getImageResId());
        } else {
            holder.ivFood.setImageResource(R.drawable.margherita_pizza);
        }

        if ("Delivered".equalsIgnoreCase(order.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#E65100"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvItemName, tvRestaurant, tvPrice, tvDate, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.iv_order_food);
            tvItemName = itemView.findViewById(R.id.tv_order_item_name);
            tvRestaurant = itemView.findViewById(R.id.tv_order_restaurant);
            tvPrice = itemView.findViewById(R.id.tv_order_price);
            tvDate = itemView.findViewById(R.id.tv_order_date);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
