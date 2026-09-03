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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // حقن كود البحث عن الأزرار
                injectAutoClickCode();
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("https://qxbroker.com/en/trade");

        btnStart.setOnClickListener(v -> startTrading());
        btnStop.setOnClickListener(v -> stopTrading());
    }

    private void injectAutoClickCode() {
        String jsCode = 
            "window.autoClick = function(signal) {" +
            "  var buttons = document.querySelectorAll('button, div[role=\"button\"], span[role=\"button\"]');" +
            "  for (var i = 0; i < buttons.length; i++) {" +
            "    var btn = buttons[i];" +
            "    var text = (btn.innerText || btn.textContent || '').toLowerCase();" +
            "    var isVisible = btn.offsetParent !== null && btn.offsetWidth > 0 && btn.offsetHeight > 0;" +
            "    if (isVisible) {" +
            "      if (signal === 'up' && (text.includes('call') || text.includes('up'))) {" +
            "        btn.click();" +
            "        console.log('✅ تم النقر على صعود');" +
            "        return true;" +
            "      }" +
            "      if (signal === 'down' && (text.includes('put') || text.includes('down'))) {" +
            "        btn.click();" +
            "        console.log('✅ تم النقر على هبوط');" +
            "        return true;" +
            "      }" +
            "    }" +
            "  }" +
            "  console.log('❌ لم يتم العثور على زر: ' + signal);" +
            "  return false;" +
            "};" +
            "console.log('✅ كود النقر التلقائي جاهز!');";
        webView.evaluateJavascript(jsCode, null);
    }

    private void startTrading() {
        if (isRunning) return;
        isRunning = true;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnStop.setVisibility(View.VISIBLE);
        Toast.makeText(this, "بدء التداول...", Toast.LENGTH_SHORT).show();

        // تشغيل الخدمة الخلفية مع إشارة البدء
        Intent serviceIntent = new Intent(this, TradingService.class);
        serviceIntent.putExtra("ACTION", "START");
        startService(serviceIntent);
    }

    private void stopTrading() {
        isRunning = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnStop.setVisibility(View.GONE);
        Toast.makeText(this, "تم إيقاف التداول", Toast.LENGTH_SHORT).show();

        Intent serviceIntent = new Intent(this, TradingService.class);
        serviceIntent.putExtra("ACTION", "STOP");
        startService(serviceIntent);
    }
}
