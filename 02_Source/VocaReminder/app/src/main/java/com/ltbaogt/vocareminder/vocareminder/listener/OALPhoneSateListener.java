package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

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
        VRLog.d(TAG, ">>>onCallStateChanged STATR, state= " + state);
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                VRLog.d(TAG, ">>>onCallStateChanged CALL_STATE_IDLE");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                VRLog.d(TAG, ">>>onCallStateChanged CALL_STATE_RINGING");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                VRLog.d(TAG, ">>>onCallStateChanged CALL_STATE_OFFHOOK");
                break;
        }
        VRLog.d(TAG, ">>>onCallStateChanged END");
    }
}
