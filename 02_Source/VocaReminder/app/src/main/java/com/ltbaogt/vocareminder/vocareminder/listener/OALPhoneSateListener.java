package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by My PC on 07/08/2016.
 */

public class OALPhoneSateListener extends PhoneStateListener {
    public static final String TAG = Define.TAG + "OALPhoneSateListener";
    private Context mContext;

    public OALPhoneSateListener(Context ctx) {
        mContext = ctx;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        Log.d(TAG, ">>>onCallStateChanged STATR, state= " + state);
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG, ">>>onCallStateChanged CALL_STATE_IDLE");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, ">>>onCallStateChanged CALL_STATE_RINGING");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d(TAG, ">>>onCallStateChanged CALL_STATE_OFFHOOK");
                break;
        }
        Log.d(TAG, ">>>onCallStateChanged END");
    }
}
