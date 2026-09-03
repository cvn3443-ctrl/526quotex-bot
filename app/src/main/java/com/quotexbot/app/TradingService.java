package com.quotexbot.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.widget.Toast;
import android.os.PowerManager;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

public class TradingService extends Service {
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private PowerManager.WakeLock wakeLock;
    private WebView webView;

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TradingService:WakeLock");
        wakeLock.acquire(10 * 60 * 1000L);

        // إنشاء WebView مخفي لتنفيذ JavaScript
        webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://qxbroker.com/en/trade");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "STOP".equals(intent.getStringExtra("ACTION"))) {
            stopTrading();
            return START_NOT_STICKY;
        }

        isRunning = true;
        Toast.makeText(this, "⏳ بدء التداول في الخلفية...", Toast.LENGTH_SHORT).show();
        startTradingLoop();
        return START_STICKY;
    }

    private void startTradingLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                // اختيار عشوائي بين صعود وهبوط
                boolean isUp = Math.random() > 0.5;
                String signal = isUp ? "up" : "down";

                // تنفيذ النقر عبر JavaScript
                String jsCode = "window.autoClick('" + signal + "');";
                webView.evaluateJavascript(jsCode, null);

                if (isRunning) {
                    handler.postDelayed(this, 5000);
                }
            }
        }, 3000);
    }

    private void stopTrading() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        Toast.makeText(this, "⏹ تم إيقاف التداول", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (webView != null) {
            webView.destroy();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
