package com.ltbaogt.vocareminder.vocareminder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by My PC on 05/08/2016.
 */

public class VRGestureListener extends SimpleOnGestureListener {
    public static final String TAG = Define.TAG + "VRGestureListener";
    private Context mContext;

    public VRGestureListener(Context ctx) {
        mContext = ctx;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, ">>>onDoubleTap START");
        dismissReminder();
        Log.d(TAG, ">>>onDoubleTap END");
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        dismissReminder();
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    private void dismissReminder() {
        if (mContext != null) {
            Intent intent = new Intent(Define.CLOSE_VOCA_REMINDER);
            mContext.sendBroadcast(intent);
        }
    }
}
