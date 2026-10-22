package com.example.assignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.assignment2.model.OrderModel;
import com.example.assignment2.utils.CartManager;
import com.example.assignment2.utils.FileHelper;

import java.io.File;
import java.util.List;

public class FileHandlingCsvActivity extends AppCompatActivity {

    private TextView tvPreview, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_handling_csv);

        Toolbar toolbar = findViewById(R.id.toolbar_csv);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Save Orders to CSV");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvPreview = findViewById(R.id.tv_csv_preview);
        tvStatus = findViewById(R.id.tv_csv_status);
        Button btnExport = findViewById(R.id.btn_export_csv);
        Button btnShare = findViewById(R.id.btn_share_csv);

        btnExport.setOnClickListener(v -> exportCsv());
        btnShare.setOnClickListener(v -> FileHelper.shareCsvFile(this));

        // Initial preview load
        previewCsv();
    }

    private void exportCsv() {
        List<OrderModel> orders = CartManager.getOrders(this);
        File file = FileHelper.saveOrdersToCsv(this, orders);
        if (file != null) {
            tvStatus.setText("✓ CSV file saved successfully!");
            Toast.makeText(this, "CSV file saved successfully!", Toast.LENGTH_SHORT).show();
            previewCsv();
        } else {
            tvStatus.setText("Failed to save CSV file.");
            Toast.makeText(this, "Failed to export CSV.", Toast.LENGTH_SHORT).show();
        }
    }

    private void previewCsv() {
        String previewText = FileHelper.readOrdersCsvPreview(this);
        tvPreview.setText(previewText);
    }
}
