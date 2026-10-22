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

public class FileHandlingTxtActivity extends AppCompatActivity {

    private TextView tvPreview, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_handling_txt);

        Toolbar toolbar = findViewById(R.id.toolbar_txt);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Save Orders to TXT");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvPreview = findViewById(R.id.tv_txt_preview);
        tvStatus = findViewById(R.id.tv_txt_status);
        Button btnSave = findViewById(R.id.btn_save_txt);
        Button btnRead = findViewById(R.id.btn_read_txt);

        btnSave.setOnClickListener(v -> saveToTxt());
        btnRead.setOnClickListener(v -> readFromTxt());
    }

    private void saveToTxt() {
        List<OrderModel> orders = CartManager.getOrders(this);
        boolean success = FileHelper.saveOrdersToTxt(this, orders);
        if (success) {
            File file = new File(getFilesDir(), "FoodieOrders.txt");
            tvStatus.setText("Order saved to " + file.getAbsolutePath());
            Toast.makeText(this, "Orders saved to TXT file!", Toast.LENGTH_SHORT).show();
            readFromTxt();
        } else {
            tvStatus.setText("Error saving TXT file.");
            Toast.makeText(this, "Failed to save TXT file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void readFromTxt() {
        String content = FileHelper.readOrdersFromTxt(this);
        tvPreview.setText(content);
    }
}
