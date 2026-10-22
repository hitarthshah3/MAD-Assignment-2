package com.example.assignment2.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.assignment2.model.OrderModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FileHelper {

    private static final String TXT_FILE_NAME = "FoodieOrders.txt";
    private static final String CSV_FILE_NAME = "FoodieOrders.csv";

    // Save Orders to TXT
    public static boolean saveOrdersToTxt(Context context, List<OrderModel> orders) {
        StringBuilder builder = new StringBuilder();
        for (OrderModel order : orders) {
            builder.append("Order Details\n");
            builder.append("--------------------\n");
            builder.append("Date: ").append(order.getDate()).append("\n");
            builder.append("Restaurant: ").append(order.getRestaurantName()).append("\n");
            builder.append("Item: ").append(order.getItemName()).append("\n");
            builder.append("Price: ₹").append((int) order.getPrice()).append("\n");
            builder.append("Status: ").append(order.getStatus()).append("\n\n");
        }

        try (FileOutputStream fos = context.openFileOutput(TXT_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(builder.toString().getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read Orders from TXT
    public static String readOrdersFromTxt(Context context) {
        StringBuilder builder = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(TXT_FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            return "No saved TXT file found or file error.";
        }
    }

    // Save Orders to CSV
    public static File saveOrdersToCsv(Context context, List<OrderModel> orders) {
        StringBuilder builder = new StringBuilder();
        builder.append("Date,Restaurant,Item,Price,Status\n");
        for (OrderModel order : orders) {
            builder.append(escapeCsv(order.getDate())).append(",");
            builder.append(escapeCsv(order.getRestaurantName())).append(",");
            builder.append(escapeCsv(order.getItemName())).append(",");
            builder.append((int) order.getPrice()).append(",");
            builder.append(escapeCsv(order.getStatus())).append("\n");
        }

        try {
            File file = new File(context.getFilesDir(), CSV_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(builder.toString().getBytes());
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Read Orders CSV Preview
    public static String readOrdersCsvPreview(Context context) {
        try {
            File file = new File(context.getFilesDir(), CSV_FILE_NAME);
            if (!file.exists()) return "No CSV file saved yet.";

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        } catch (Exception e) {
            return "Error reading CSV file.";
        }
    }

    // Share CSV file via FileProvider
    public static void shareCsvFile(Context context) {
        File file = new File(context.getFilesDir(), CSV_FILE_NAME);
        if (!file.exists()) return;

        Uri contentUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share CSV File"));
    }

    private static String escapeCsv(String text) {
        if (text == null) return "";
        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }
        return text;
    }
}
