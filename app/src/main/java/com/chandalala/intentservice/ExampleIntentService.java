package com.chandalala.intentservice;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class ExampleIntentService extends IntentService {

    private PowerManager.WakeLock wakeLock;

    public static final String TAG = "ExampleIntentService";

    public ExampleIntentService() {
        super("ExampleIntentService");
        setIntentRedelivery(true);  // restarts the service if it killed
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");

        //We can activate a wake lock as soon as the service starts
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK
                ,"ExampleApp:Wakelock");

        wakeLock.acquire(10*60*1000L /*10 minutes*/);
        Log.d(TAG, "Wakelock acquired");

        Notification notification = new Notification.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Example IntentService")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_android)
                .build();

        startForeground(1, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        //release wakelock here
        wakeLock.release();
        Log.d(TAG, "Wakelock released");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent:");

        String input = intent.getStringExtra("inputExtra");

        for (int i = 0; i < 10; i++){
            Log.d(TAG, input+" - " + i);
            SystemClock.sleep(1000);
        }

    }

}
