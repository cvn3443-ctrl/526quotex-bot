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
            "function findAndClick(targetText) {" +
            "  // البحث في جميع العناصر" +
            "  var elements = document.querySelectorAll('*');" +
            "  for (var i = 0; i < elements.length; i++) {" +
            "    var el = elements[i];" +
            "    var text = el.innerText || el.textContent || '';" +
            "    // التأكد من أن العنصر قابل للنقر ومرئي" +
            "    var isClickable = el.onclick !== null || el.getAttribute('role') === 'button' || el.tagName === 'BUTTON';" +
            "    var isVisible = el.offsetParent !== null && el.offsetWidth > 0 && el.offsetHeight > 0;" +
            "    if (isClickable && isVisible && text.trim() === targetText) {" +
            "      el.click();" +
            "      console.log('✅ تم النقر على: ' + targetText);" +
            "      return true;" +
            "    }" +
            "  }" +
            "  // محاولة البحث عن عنصر يحتوي على النص (إذا لم يطابق تماماً)" +
            "  for (var i = 0; i < elements.length; i++) {" +
            "    var el = elements[i];" +
            "    var text = el.innerText || el.textContent || '';" +
            "    var isVisible = el.offsetParent !== null && el.offsetWidth > 0 && el.offsetHeight > 0;" +
            "    if (isVisible && text.includes(targetText)) {" +
            "      el.click();" +
            "      console.log('✅ تم النقر على عنصر يحتوي: ' + targetText);" +
            "      return true;" +
            "    }" +
            "  }" +
            "  console.log('❌ لم يتم العثور على: ' + targetText);" +
            "  return false;" +
            "}" +
            "window.clickUp = function() {" +
            "  return findAndClick('Call') || findAndClick('Up') || findAndClick('صعود');" +
            "};" +
            "window.clickDown = function() {" +
            "  return findAndClick('Put') || findAndClick('Down') || findAndClick('هبوط');" +
            "};" +
            "console.log('✅ كود النقر الشامل جاهز!');";
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
