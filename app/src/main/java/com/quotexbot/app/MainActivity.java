package com.quotexbot.app;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button btnStart, btnStop;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("https://qxbroker.com/en/trade");

        btnStart.setOnClickListener(v -> startTrading());
        btnStop.setOnClickListener(v -> stopTrading());
    }

    private void startTrading() {
        if (isRunning) return;
        isRunning = true;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnStop.setVisibility(View.VISIBLE);
        Toast.makeText(this, "▶ بدء التداول...", Toast.LENGTH_SHORT).show();

        // تشغيل الخدمة الخلفية
        Intent serviceIntent = new Intent(this, TradingService.class);
        startService(serviceIntent);
    }

    private void stopTrading() {
        isRunning = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnStop.setVisibility(View.GONE);
        Toast.makeText(this, "⏹ تم إيقاف التداول", Toast.LENGTH_SHORT).show();

        // إيقاف الخدمة الخلفية
        Intent serviceIntent = new Intent(this, TradingService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // التطبيق يفضل شغال حتى في الخلفية
    }
}
