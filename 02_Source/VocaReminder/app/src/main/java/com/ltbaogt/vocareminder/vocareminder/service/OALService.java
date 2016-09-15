package com.ltbaogt.vocareminder.vocareminder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.provider.ProviderWrapper;
import com.ltbaogt.vocareminder.vocareminder.receiver.OALBroadcastReceiver;

/**
 * Created by My PC on 04/08/2016.
 */

public class OALService extends Service {

    private static final String TAG = Define.TAG + "OALService";
    OALBroadcastReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, ">>>onCreate START");
        super.onCreate();
//        mInstance = this;
        mReceiver = new OALBroadcastReceiver(getApplicationContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Define.VOCA_ACTION_OPEN_VOCA_REMINDER);
        filter.addAction(Define.VOCA_ACTION_CLOSE_VOCA_REMINDER);
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
        unregisterReceiver(mReceiver);
        mReceiver = null;
        ProviderWrapper wrapper = new ProviderWrapper(getApplicationContext());
        if (wrapper.getServiceRunningStatus() == 1) {
            Intent i = new Intent(getApplicationContext(), this.getClass());
            getApplicationContext().startService(i);
        }
        Log.d(TAG, ">>>onDestroy END");
    }
}
