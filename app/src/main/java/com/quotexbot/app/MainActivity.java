package com.quotexbot.app;

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

        // ربط العناصر
        webView = findViewById(R.id.webView);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        // إعداد WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // تحميل منصة Quotex
        webView.loadUrl("https://qxbroker.com/en/trade");

        // زر البدء
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrading();
            }
        });

        // زر الإيقاف
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTrading();
            }
        });
    }

    private void startTrading() {
        if (isRunning) return;
        isRunning = true;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnStop.setVisibility(View.VISIBLE);
        Toast.makeText(this, "بدء التداول...", Toast.LENGTH_SHORT).show();

        // حقن كود النقر التلقائي في المنصة
        String jsCode = 
            "if (typeof botInterval !== 'undefined') clearInterval(botInterval);" +
            "console.log('✅ بدء التداول التلقائي (وهمي)');" +
            "botInterval = setInterval(function() {" +
            "  var signal = Math.random() > 0.5 ? 'up' : 'down';" +
            "  console.log('🖱️ إشارة: ' + signal);" +
            "  // محاكاة النقر (سنضيف النقر الفعلي لاحقاً)" +
            "  var x = signal === 'up' ? 500 : 500;" +
            "  var y = signal === 'up' ? 900 : 1100;" +
            "  // تنبيه في الواجهة (للتجربة)" +
            "  var alertMsg = '🖱️ تنفيذ صفقة: ' + signal;" +
            "  console.log(alertMsg);" +
            "}, 5000);" + // كل 5 ثوانٍ
            "setTimeout(function() { console.log('⏳ التداول مستمر...'); }, 1000);";
        
        webView.evaluateJavascript(jsCode, null);
    }

    private void stopTrading() {
        isRunning = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnStop.setVisibility(View.GONE);
        Toast.makeText(this, "تم إيقاف التداول", Toast.LENGTH_SHORT).show();

        // إيقاف التداول
        String jsCode = 
            "if (typeof botInterval !== 'undefined') {" +
            "  clearInterval(botInterval);" +
            "  botInterval = undefined;" +
            "  console.log('⏹ تم إيقاف التداول');" +
            "}";
        webView.evaluateJavascript(jsCode, null);
    }

    // السماح للتطبيق بالعمل في الخلفية
    @Override
    protected void onPause() {
        super.onPause();
        // التطبيق سيستمر في العمل حتى لو تم تصغيره
    }

    @Override
    protected void onResume() {
        super.onResume();
        // عند العودة للتطبيق، تحديث الحالة
        if (isRunning) {
            Toast.makeText(this, "التداول مستمر...", Toast.LENGTH_SHORT).show();
        }
    }
}
