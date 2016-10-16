package com.ltbaogt.vocareminder.vocareminder.listener;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.widget.CompoundButton;

import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import java.lang.ref.WeakReference;

/**
 * Created by My PC on 21/08/2016.
 */

public class ServiceRunningListener implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = Define.TAG + "ServiceRunningListener";
    private WeakReference<Activity> mActivity;

    public ServiceRunningListener(Activity a) {
        mActivity = new WeakReference<>(a);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (mActivity != null && mActivity.get() != null && mActivity.get() instanceof MainActivity) {
            MainActivity activity = ((MainActivity) mActivity.get());
            if (b) {
                if (canDrawOverlays()) {
                    activity.startVRService();
                } else {
                    activity.startActivityForDrawOverlay();
                }
            } else {
                activity.stopVRService();
            }
        } else {
            VRLog.d(TAG, "Unable to start/stop service");
        }
    }

    /**
     * Check App can draw overlay or not
     */
    private boolean canDrawOverlays() {
        boolean canDraw = true;
        if (Build.VERSION.SDK_INT >= 23) {
            canDraw = Settings.canDrawOverlays(mActivity.get().getApplicationContext());
        }
        VRLog.d(TAG, ">>>canDrawOverlays canDraw= " + canDraw);
        return canDraw;
    }
}
