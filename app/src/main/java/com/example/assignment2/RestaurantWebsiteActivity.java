package com.example.assignment2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RestaurantWebsiteActivity extends AppCompatActivity {

    private EditText etUrl;
    private WebView webView;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_website);

        Toolbar toolbar = findViewById(R.id.toolbar_web);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Restaurant Website");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etUrl = findViewById(R.id.et_website_url);
        webView = findViewById(R.id.webview);
        pbLoading = findViewById(R.id.pb_web_loading);
        Button btnOpen = findViewById(R.id.btn_open_website);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(RestaurantWebsiteActivity.this, "Failed to load website. Please check internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

        btnOpen.setOnClickListener(v -> loadWebsite());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        // Load initial URL
        loadWebsite();
    }

    private void loadWebsite() {
        String url = etUrl.getText().toString().trim();
        if (url.isEmpty()) {
            etUrl.setError("URL required");
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        if (!Patterns.WEB_URL.matcher(url).matches()) {
            etUrl.setError("Invalid URL");
            return;
        }

        webView.loadUrl(url);
    }
}
