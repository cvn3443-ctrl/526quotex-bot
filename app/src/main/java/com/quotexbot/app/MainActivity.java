package com.quotexbot.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button btnStart, btnStop;
    private TextView statusText;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط العناصر
        webView = findViewById(R.id.webView);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        statusText = findViewById(R.id.status_text);

        // إعداد WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // تحميل منصة Quotex مباشرة
        webView.loadUrl("https://qxbroker.com/en/trade");

        // أزرار التحكم
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = true;
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                statusText.setText("يعمل...");
                // استدعاء دالة البدء في صفحة الويب
                webView.evaluateJavascript("javascript:startBot()", null);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                statusText.setText("متوقف");
                webView.evaluateJavascript("javascript:stopBot()", null);
            }
        });
    }
}
