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
            "function findAndClick(selectors) {" +
            "  for (var i = 0; i < selectors.length; i++) {" +
            "    var element = document.querySelector(selectors[i]);" +
            "    if (element) {" +
            "      element.click();" +
            "      console.log('✅ تم النقر باستخدام: ' + selectors[i]);" +
            "      return true;" +
            "    }" +
            "  }" +
            "  console.log('❌ لم يتم العثور على أي زر');" +
            "  return false;" +
            "}" +
            "window.clickUp = function() {" +
            "  var selectors = [" +
            "    'button[aria-label=\"Up\"]'," +
            "    'button[aria-label=\"Call\"]'," +
            "    'button[class*=\"call\"]'," +
            "    'button[class*=\"up\"]'," +
            "    'button:contains(\"Up\")'," +
            "    'button:contains(\"Call\")'" +
            "  ];" +
            "  return findAndClick(selectors);" +
            "};" +
            "window.clickDown = function() {" +
            "  var selectors = [" +
            "    'button[aria-label=\"Down\"]'," +
            "    'button[aria-label=\"Put\"]'," +
            "    'button[class*=\"put\"]'," +
            "    'button[class*=\"down\"]'," +
            "    'button:contains(\"Down\")'," +
            "    'button:contains(\"Put\")'" +
            "  ];" +
            "  return findAndClick(selectors);" +
            "};" +
            "console.log('✅ كود النقر المطور جاهز!');";
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
