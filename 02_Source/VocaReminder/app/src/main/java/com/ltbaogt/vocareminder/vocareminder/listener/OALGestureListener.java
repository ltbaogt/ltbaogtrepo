package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by My PC on 05/08/2016.
 */

public class OALGestureListener extends SimpleOnGestureListener {
    public static final String TAG = Define.TAG + "OALGestureListener";
    private Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;

    private OALGestureListener() {

    }
    public OALGestureListener(Context ctx) {
        mContext = ctx;
        SharedPreferences shared = mContext.getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE);
        mScreenWidth = shared.getInt(Define.REF_SCREEN_SIZE_WIDTH,0);
        mScreenHeight = shared.getInt(Define.REF_SCREEN_SIZE_HEIGHT,0);
        Log.d("TAG", ">>>OALGestureListener screen size= (" + mScreenWidth + "x" + mScreenHeight);
    }

    public void setOnOpenSettingPanelListener(OnOpenSettingPanel listener) {
        mOpenSettingPanel = listener;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, ">>>onDoubleTap START");
        dismissReminder();
        Log.d(TAG, ">>>onDoubleTap END");
        return super.onDoubleTap(e);
    }

    public interface OnOpenSettingPanel {

        void onOpenSettingPanel();
    }

    OnOpenSettingPanel mOpenSettingPanel;
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG,">>>onFling ScreenWidth= " + mScreenWidth
                             + " ScreenHeight= " + mScreenHeight
                             + " e1.getY= " + e1.getY()
                             + " e2.getY= " + e2.getY());
        if (e1.getY() >= (mScreenHeight - 500)
                && e2.getY() >= (mScreenHeight - 500)
                && e1.getY() > e2.getY()) {
            Log.d(TAG, ">>>onFling show setting");
            if (mOpenSettingPanel != null) {
                mOpenSettingPanel.onOpenSettingPanel();
            }
        } else {
            Log.d(TAG, ">>>onFling dismiss reminder");
            dismissReminder();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    private void dismissReminder() {
        if (mContext != null) {
            Intent intent = new Intent(Define.VOCA_ACTION_CLOSE_VOCA_REMINDER);
            mContext.sendBroadcast(intent);
        }
    }
}
