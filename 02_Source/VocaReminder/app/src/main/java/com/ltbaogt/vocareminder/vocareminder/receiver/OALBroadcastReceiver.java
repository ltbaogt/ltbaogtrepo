package com.ltbaogt.vocareminder.vocareminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.handler.ReceiverHandler;
import com.ltbaogt.vocareminder.vocareminder.listener.OALGestureListener;
import com.ltbaogt.vocareminder.vocareminder.listener.OpenPanelListener;
import com.ltbaogt.vocareminder.vocareminder.provider.ProviderWrapper;
import com.ltbaogt.vocareminder.vocareminder.runnable.ShowHideViewRunnable;
import com.ltbaogt.vocareminder.vocareminder.shareref.OALShareReferenceHepler;

/**
 * Created by My PC on 04/08/2016.
 */

public class OALBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = Define.TAG + "OALBroadcastReceiver";
    private View mReminderLayout;
    private ToggleButton mToggleButton;
    private ImageView mButtonOpenApp;
    private TextView mTvWord;
    private TextView mTvPronun;
    private TextView mTvSentence;
    WindowManager mWindowManager;
    Context mContext;
    OALGestureListener mDoubletabDetector;
    GestureDetector mGestureDetector;
    TelephonyManager mTelephonyManager;
    WindowManager.LayoutParams mLayoutParam;

    private ReceiverHandler mServiceHandler;

    private OpenPanelListener mOpenPanelListener;

    private ShowHideViewRunnable mShowHideRunnable;

    public OALBroadcastReceiver(Context ctx) {
        Log.d(TAG, ">>>OALBroadcastReceiver init");
        mContext = ctx;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mReminderLayout = layoutInflater.inflate(R.layout.main_reminder_layout, null, false);
        mTvWord = (TextView) mReminderLayout.findViewById(R.id.tv_vocabulary);
        mTvPronun = (TextView) mReminderLayout.findViewById(R.id.tv_pronunciation);
        mTvSentence = (TextView) mReminderLayout.findViewById(R.id.tv_sentence);
        mToggleButton = (ToggleButton) mReminderLayout.findViewById(R.id.toggle);
        mButtonOpenApp = (ImageView) mReminderLayout.findViewById(R.id.btn_setting);

        AssetManager assetManager = mContext.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_BOLD);
        mTvWord.setTypeface(typeface);

        typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_REGILAR);
        mTvSentence.setTypeface(typeface);
        mReminderLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mReminderLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initGestureDetection();
            }
        });

        mLayoutParam = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        //TOAST: ability to view notification
        //lp.type = WindowManager.LayoutParams.TYPE_TOAST;
        //SYSTEM_ERROR: doesn't ability to view notification
        mLayoutParam.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mLayoutParam.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        //Setup in/out animation for reminder
        mLayoutParam.windowAnimations = android.R.style.Animation_Toast;

        setupReminderEvent();
        initPhoneStateChanged();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>>onReceive START");
        String action = intent.getAction();
        int callState = mTelephonyManager.getCallState();
        Log.d(TAG, ">>>onReceive callState= " + callState);
        if (Intent.ACTION_SCREEN_ON.equals(action) && (callState == TelephonyManager.CALL_STATE_IDLE)
                || Define.VOCA_ACTION_OPEN_VOCA_REMINDER.equals(action)) {
            //if (mReminderLayout != null) return;
            //Setup Gesture action when reminder layout inflated
            setupContentView();
            //Add try/catch in order to prevent user add  reminder many times
            try {
                mWindowManager.addView(mReminderLayout, mLayoutParam);
            } catch (IllegalStateException e) {
                Log.d(TAG, ">>>onReceive No need add new Reminder");
            }
            ProviderWrapper providerWrapper = new ProviderWrapper(mContext);
            int dismissTime = providerWrapper.getDismissTime();
            if (dismissTime > 0) {
                Log.d(TAG, ">>>onReceive sendMessage to dismiss reminder after " + dismissTime);
                mServiceHandler.sendEmptyMessageDelayed(Define.HANDLER_WHAT_AUTO_DISMISS,dismissTime);
            }

        } else if (Define.VOCA_ACTION_CLOSE_VOCA_REMINDER.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
            try {
                dismissReminderLayout();
            } catch (NullPointerException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        Log.d(TAG, ">>>onReceive END");
    }

    private void dismissReminderLayout() {
        try {
            mWindowManager.removeViewImmediate(mReminderLayout);
            mWindowManager.addView(null, null);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "View isn't attached to window manager");
        }
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
            mOpenPanelListener = new OpenPanelListener(mReminderLayout);
            mDoubletabDetector = new OALGestureListener(mContext, mReminderLayout.getMeasuredWidth(), mReminderLayout.getMeasuredHeight());
            mDoubletabDetector.setOnOpenSettingPanelListener(mOpenPanelListener);
        }
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(mContext, mDoubletabDetector);
        }

    }

    private void setupContentView() {
        if (mReminderLayout == null) return;
        ProviderWrapper providerWrapper = new ProviderWrapper(mContext);

        mReminderLayout.setBackgroundColor(providerWrapper.getColorTheme());
        Word w = providerWrapper.getRandomWord();
        if (w != null) {
            mTvWord.setText(w.getWordName());
            mTvPronun.setText(w.getPronunciation());
            mTvSentence.setText(w.getDefault_Meaning());
        } else {
            Log.d(TAG, "Dictionary is empty");
        }
    }

    private void setupReminderEvent() {
        if (mReminderLayout == null) return;

        mReminderLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, ">>>onTouch");
                if (mReminderLayout != null) {
                    View v = mReminderLayout.findViewById(R.id.panel_setting);
                    mShowHideRunnable.setView(v);
                    v.animate().alpha(0).setDuration(2000).withEndAction(mShowHideRunnable).start();
                }
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        mButtonOpenApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, ">>>BTN_SETTING>>>onClick");

                if (mContext != null) {
                    Intent intent = new Intent(Define.VOCA_ACTION_CLOSE_VOCA_REMINDER);
                    mContext.sendBroadcast(intent);
                }
                Intent mainScreen = new Intent(mContext, MainActivity.class);
                mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(mainScreen);
            }
        });

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mTvSentence.setAlpha(0);
                    mTvSentence.setVisibility(View.VISIBLE);
                    mTvSentence.animate().alpha(1).setDuration(500).start();
                } else {
                    mShowHideRunnable.setView(mTvSentence);
                    mTvSentence.animate().alpha(0).setDuration(500).withEndAction(mShowHideRunnable).start();

                }
            }
        });
        mServiceHandler = new ReceiverHandler(mContext);
        mShowHideRunnable = new ShowHideViewRunnable();
    }

}
