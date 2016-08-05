package com.ltbaogt.vocareminder.vocareminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by My PC on 04/08/2016.
 */

public class VRBroadcastReceiver extends BroadcastReceiver {

    View mReminderLayout;
    WindowManager mWindowManager;
    Context mContext;
    VRGestureListener mDoubletabDetector;
    GestureDetector mGestureDetector;

    private DummyDB db;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("VR.VRBroadcastReceiver", ">>>onReceive");
        mContext = context;
        String action = intent.getAction();

        if (db == null) {
            db = new DummyDB(mContext);
        }
        if (mDoubletabDetector == null) {
            mDoubletabDetector = new VRGestureListener(mContext);
        }
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(mContext, mDoubletabDetector);
        }

        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            if (mReminderLayout != null) return;
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mReminderLayout = li.inflate(R.layout.main_reminder_layout, null, false);
            //Setup Gesture action
            setupReminderEvent();
            setupContent();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            //TOAST: ability to view notification
            //lp.type = WindowManager.LayoutParams.TYPE_TOAST;
            //SYSTEM_ERROR: doesn't ability to view notification
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            //Setup in/out animation for reminder
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

    private void setupContent() {
        if (mReminderLayout == null) return;
        TextView tvWord = (TextView) mReminderLayout.findViewById(R.id.tv_vocabulary);
        TextView tvSentence = (TextView) mReminderLayout.findViewById(R.id.tv_sentence);
        int radom = (int) System.currentTimeMillis() % 10;
        Word w = db.getWordAt(radom);
        tvWord.setText(w.getName());
        tvSentence.setText(w.getSentence());
    }

    private void setupReminderEvent() {
        if (mReminderLayout == null) return;
        mReminderLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("VR.VRBroadcastReceiver", ">>>onTouch");
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

}
