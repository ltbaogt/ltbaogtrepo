package com.ltbaogt.vocareminder.vocareminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by My PC on 04/08/2016.
 */

public class VRBroadcastReceiver extends BroadcastReceiver implements View.OnClickListener {

    View mReminderLayout;
    WindowManager mWindowManager;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("VR.VRBroadcastReceiver", ">>>onReceive");
        mContext = context;
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            if (mReminderLayout != null) return;
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mReminderLayout = li.inflate(R.layout.main_reminder_layout, null, false);
            setupReminderEvent();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            lp.windowAnimations = android.R.style.Animation_Toast;
            mWindowManager.addView(mReminderLayout, lp);
        } else if (Define.CLOSE_VOCA_REMINDER.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
            try {
                if (mReminderLayout == null) return;
                mWindowManager.removeViewImmediate(mReminderLayout);
                mReminderLayout = null;
            } catch (NullPointerException e) {
                Log.e("VR.VRBroadcastReceiver", Log.getStackTraceString(e));
            }
        }
    }

    private void setupReminderEvent() {
        if (mReminderLayout == null) return;
        Button btn = (Button) mReminderLayout.findViewById(R.id.btnClose);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("VR.VRBroadcastReceiver", ">>>onClick");
        Intent intent = new Intent(Define.CLOSE_VOCA_REMINDER);
        mContext.sendBroadcast(intent);
    }
}
