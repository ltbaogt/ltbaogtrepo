package com.ltbaogt.vocareminder.vocareminder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by My PC on 04/08/2016.
 */

public class VRService extends Service {

    private static final String TAG = Define.TAG + "VRService";
    private static VRService mInstance = null;
    VRBroadcastReceiver mReceiver = new VRBroadcastReceiver();

    public static boolean isStarted() {
        return  (mInstance != null);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, ">>>onCreate START");
        super.onCreate();
        mInstance = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Define.OPEN_VOCA_REMINDER);
        filter.addAction(Define.CLOSE_VOCA_REMINDER);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver,filter);
        Log.d(TAG, ">>>onCreate END");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, ">>>onStartCommand START");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, ">>>onDestroy START");
        super.onDestroy();
        mInstance = null;
        unregisterReceiver(mReceiver);
        Log.d(TAG, ">>>onDestroy END");
    }

}
