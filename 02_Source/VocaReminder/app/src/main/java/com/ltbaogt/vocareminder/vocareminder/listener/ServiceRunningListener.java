package com.ltbaogt.vocareminder.vocareminder.listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;

/**
 * Created by My PC on 21/08/2016.
 */

public class ServiceRunningListener implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = Define.TAG + "ServiceRunningListener";
    private Activity mActivity;

    public ServiceRunningListener(Activity a) {
        mActivity = a;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (mActivity != null && mActivity instanceof MainActivity) {
            MainActivity activity = ((MainActivity) mActivity);
            if (b) {
                activity.startVRService();
            } else {
                activity.stopVRService();
            }
        } else {
            Log.d(TAG, "Unable to start/stop service");
        }
    }
}
