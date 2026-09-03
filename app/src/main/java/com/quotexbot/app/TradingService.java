package com.quotexbot.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.widget.Toast;
import android.os.PowerManager;
import android.content.Context;

public class TradingService extends Service {
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private PowerManager.WakeLock wakeLock;

    // ⚠️ إحداثيات الأزرار (عدلها حسب شاشتك)
    private static final int UP_X = 500;   // مكان زر "Up"
    private static final int UP_Y = 900;
    private static final int DOWN_X = 500; // مكان زر "Down"
    private static final int DOWN_Y = 1100;

    @Override
    public void onCreate() {
        super.onCreate();
        // منع السكون
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TradingService:WakeLock");
        wakeLock.acquire(10 * 60 * 1000L); // 10 دقائق
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        Toast.makeText(this, "⏳ بدء التداول في الخلفية...", Toast.LENGTH_SHORT).show();
        startTradingLoop();
        return START_STICKY; // يعيد التشغيل إذا توقف
    }

    private void startTradingLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                boolean isUp = Math.random() > 0.5;
                if (isUp) {
                    performTap(UP_X, UP_Y);
                } else {
                    performTap(DOWN_X, DOWN_Y);
                }

                if (isRunning) {
                    handler.postDelayed(this, 5000); // كل 5 ثواني
                }
            }
        }, 3000);
    }

    private void performTap(int x, int y) {
        try {
            Process process = Runtime.getRuntime().exec("su -c input tap " + x + " " + y);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        Toast.makeText(this, "⏹ تم إيقاف التداول", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
