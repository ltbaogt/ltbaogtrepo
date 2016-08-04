package com.ltbaogt.vocareminder.vocareminder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by My PC on 04/08/2016.
 */

public class VRService extends Service {

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
        super.onCreate();
        mInstance = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Define.OPEN_VOCA_REMINDER);
        filter.addAction(Define.CLOSE_VOCA_REMINDER);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstance = null;
        unregisterReceiver(mReceiver);
    }
}
