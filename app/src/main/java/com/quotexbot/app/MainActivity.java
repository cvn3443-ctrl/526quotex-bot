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

        webView = findViewById(R.id.webView);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // حقن كود النقر بعد تحميل المنصة
                injectClickCode();
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("https://qxbroker.com/en/trade");

        btnStart.setOnClickListener(v -> startTrading());
        btnStop.setOnClickListener(v -> stopTrading());
    }

    private void injectClickCode() {
        String jsCode =
            "function simulateClick(selector) {" +
            "  var element = document.querySelector(selector);" +
            "  if (element) {" +
            "    element.click();" +
            "    console.log('✅ تم النقر على: ' + selector);" +
            "    return true;" +
            "  } else {" +
            "    console.log('❌ لم يتم العثور على: ' + selector);" +
            "    return false;" +
            "  }" +
            "}" +
            "window.clickUp = function() {" +
            "  return simulateClick('button[aria-label=\"Up\"]');" +
            "};" +
            "window.clickDown = function() {" +
            "  return simulateClick('button[aria-label=\"Down\"]');" +
            "};" +
            "console.log('✅ كود النقر جاهز!');";
        webView.evaluateJavascript(jsCode, null);
    }

    private void startTrading() {
        if (isRunning) return;
        isRunning = true;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnStop.setVisibility(View.VISIBLE);
        Toast.makeText(this, "بدء التداول...", Toast.LENGTH_SHORT).show();

        String jsCode =
            "if (typeof botInterval !== 'undefined') clearInterval(botInterval);" +
            "console.log('✅ بدء التداول التلقائي (وهمي)');" +
            "botInterval = setInterval(function() {" +
            "  var signal = Math.random() > 0.5 ? 'Up' : 'Down';" +
            "  console.log('🖱️ إشارة: ' + signal);" +
            "  if (signal === 'Up') {" +
            "    window.clickUp();" +
            "  } else {" +
            "    window.clickDown();" +
            "  }" +
            "}, 5000);";
        webView.evaluateJavascript(jsCode, null);
    }

    private void stopTrading() {
        isRunning = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnStop.setVisibility(View.GONE);
        Toast.makeText(this, "تم إيقاف التداول", Toast.LENGTH_SHORT).show();

        String jsCode =
            "if (typeof botInterval !== 'undefined') {" +
            "  clearInterval(botInterval);" +
            "  botInterval = undefined;" +
            "  console.log('⏹ تم إيقاف التداول');" +
            "}";
        webView.evaluateJavascript(jsCode, null);
    }
                       }
