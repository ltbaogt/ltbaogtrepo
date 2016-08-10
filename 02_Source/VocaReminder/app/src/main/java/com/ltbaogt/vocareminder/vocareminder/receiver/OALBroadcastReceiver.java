package com.ltbaogt.vocareminder.vocareminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.OALGestureListener;
import com.ltbaogt.vocareminder.vocareminder.shareref.OALShareReferenceHepler;

/**
 * Created by My PC on 04/08/2016.
 */

public class OALBroadcastReceiver extends BroadcastReceiver implements OALGestureListener.OnOpenSettingPanel {

    public static final String TAG = Define.TAG + "OALBroadcastReceiver";
    View mReminderLayout;
    WindowManager mWindowManager;
    Context mContext;
    OALGestureListener mDoubletabDetector;
    GestureDetector mGestureDetector;
    TelephonyManager mTelephonyManager;
    OALShareReferenceHepler mOALShareReferenceHepler;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>>onReceive START");
        mContext = context;
        String action = intent.getAction();
        initPhoneStateChanged();
        int callState = mTelephonyManager.getCallState();
        Log.d(TAG, ">>>onReceive callState= " + callState);
        if (Intent.ACTION_SCREEN_ON.equals(action) && (callState == TelephonyManager.CALL_STATE_IDLE)) {
            if (mReminderLayout != null) return;
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mReminderLayout = li.inflate(R.layout.main_reminder_layout, null, false);
//            mReminderLayout
            //
            mReminderLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mReminderLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    initGestureDetection();
                }
            });
            initSharedReferences();
            //Setup Gesture action when reminder layout inflated
            setupReminderEvent();
            setupContentView();

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
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        Log.d(TAG, ">>>onReceive END");
    }

    private void initPhoneStateChanged() {
        //Get phone state
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    private void initGestureDetection() {
        //Detect double and fling
        if (mDoubletabDetector == null) {
            mDoubletabDetector = new OALGestureListener(mContext, mReminderLayout.getMeasuredWidth(), mReminderLayout.getMeasuredHeight());
            mDoubletabDetector.setOnOpenSettingPanelListener(this);
        }
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(mContext, mDoubletabDetector);
        }
    }

    private void initSharedReferences() {
        //Instance SharedReferences
        if (mOALShareReferenceHepler == null) {
            mOALShareReferenceHepler = new OALShareReferenceHepler(mContext);
        }
    }

    private void setupContentView() {
        if (mReminderLayout == null) return;
        mReminderLayout.setBackgroundColor(mOALShareReferenceHepler.getThemeColor());
        TextView tvWord = (TextView) mReminderLayout.findViewById(R.id.tv_vocabulary);
        TextView tvSentence = (TextView) mReminderLayout.findViewById(R.id.tv_sentence);
        OALBLL bl = new OALBLL(mContext);
        int totalWord = bl.getCount();
        if (totalWord <= 0) return;
        int random = ((int) System.currentTimeMillis() % totalWord) + 1;

        Log.d(TAG, ">>>setupContentView random= " + random +
                ", totalWord= " + totalWord);
        Word w = bl.getWordById(random);
        tvWord.setText(w.getWordName());
        tvSentence.setText(w.getDefault_Meaning());
    }

    private void setupReminderEvent() {
        if (mReminderLayout == null) return;
        mReminderLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, ">>>onTouch");
                if (mReminderLayout != null) {
                    View v = mReminderLayout.findViewById(R.id.panel_setting);
                    v.setVisibility(View.INVISIBLE);
                }
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        ImageView btn = (ImageView) mReminderLayout.findViewById(R.id.btn_setting);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, ">>>BTN_SETTING>>>onClick");

                if (mContext != null) {
                    Intent intent = new Intent(Define.CLOSE_VOCA_REMINDER);
                    mContext.sendBroadcast(intent);
                }
                Intent mainScreen = new Intent(mContext, MainActivity.class);
                mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(mainScreen);
            }
        });
    }

    @Override
    public void onOpenSettingPanel() {
        View v = mReminderLayout.findViewById(R.id.panel_setting);
        v.setVisibility(View.VISIBLE);
    }
}
