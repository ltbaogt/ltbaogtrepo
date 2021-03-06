package com.ryutb.speakingtime.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ryutb.speakingtime.sql.VCDatabaseOpenHelper;
import com.ryutb.speakingtime.util.Define;
import com.ryutb.speakingtime.R;
import com.ryutb.speakingtime.voicecontroller.Rooster;
import com.ryutb.speakingtime.voicecontroller.ViNoneNowRooster;
import com.ryutb.speakingtime.voicecontroller.ViRooster;
import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.view.AlarmView;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public class AlarmActivity extends AppCompatActivity {

    public static final String TAG = "MyClock";
    ViRooster mViRooster;
    private boolean mIsRepeate;
    private AlarmView mAlarmView;
    private Handler mClockHandler;
    private TextView mTextViewClock;

    private AlarmObject mAlarmObject;
    private VCDatabaseOpenHelper mDbHelper;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mIsRepeate = intent.getBooleanExtra(Define.EXTRA_REPEAT_ALARM, false);
        Log.d(TAG, ">>>onNewIntent mIsRepeate= " + mIsRepeate);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, ">>>onCreate START");
        super.onCreate(savedInstanceState);
        mDbHelper = new VCDatabaseOpenHelper(getApplicationContext());
        boolean isStart = getIntent().getBooleanExtra(Define.EXTRA_START_FROM_ALARM_MANAGER, false);
        mIsRepeate = getIntent().getBooleanExtra(Define.EXTRA_REPEAT_ALARM, false);
        getIntent().removeExtra(Define.EXTRA_START_FROM_ALARM_MANAGER);
        //create new instance of alarm object in order to get settings
        int alarmId = getIntent().getIntExtra(Define.EXTRA_ALARM_ID, 0);
        mAlarmObject = mDbHelper.getAlarmById(alarmId);
        Log.d(TAG, ">>>onCreate alarmDisplayed= " + mAlarmObject.toString());
        if (isStart || mIsRepeate) {
            mViRooster = new ViNoneNowRooster(getApplicationContext(), mAlarmObject, true, Rooster.ALARM_SPEAK_TYPE_NOW_TIME);
            mViRooster.setIs24Hour(mAlarmObject.getAlarmIs24Hour());
            mViRooster.setOnSpeakCompleted(new Rooster.OnSpeakCompleted() {
                @Override
                public void onSpeakCompleted() {
                    Log.d(TAG, ">>>onSpeakCompleted");
                }

                @Override
                public void onRepeateCompleted() {
                    Log.d(TAG, ">>>onRepeateCompleted");
                    finish();
                }
            });
            doSpeakTime();
        }
    }

    public void stopSpeaking(View v) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.cancel();
        getWindowManager().removeView(mAlarmView);
        mViRooster.cancelRepeat();
        mClockHandler.removeCallbacksAndMessages(null);
        mClockHandler = null;
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, ">>>onResume mIsRepeate= " + mIsRepeate);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        mClockHandler = new Handler();
        mClockHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mTextViewClock != null) {
                    mTextViewClock.setText(String.format(Locale.US,"%1$02d : %2$02d : %3$02d"
                            , getHour(), mViRooster.getMinute(), mViRooster.getSecond()));
                }
                mClockHandler.postDelayed(this, 1000);
            }
        });

        if (mIsRepeate) {
            doSpeakTime();
            mIsRepeate = false;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        displayAlarm();
    }

    private void displayAlarm() {
        Log.d(TAG, ">>>displayAlarm START");
        mAlarmView = (AlarmView)((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alarm_activity_layout, null, false);
        mTextViewClock = (TextView) mAlarmView.findViewById(R.id.clock);
        RippleBackground rippleBackground=(RippleBackground) mAlarmView.findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        layoutParams.windowAnimations = android.R.style.Animation_Toast;
        getWindowManager().addView(mAlarmView, layoutParams);
        Log.d(TAG, ">>>displayAlarm END");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    stopSpeaking(null);
                    break;
            }
        }
        return true;
    }

    public void doSpeakTime() {
        if (mViRooster != null) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //               //will start in 0s, vibration for 2000s, turn it off for 500s, turn it in 1000s, turn it off for 1000s
            vibrator.vibrate(new long[]{      0,               2000,                  500 ,            1000 ,                 1000}, 0);
            try {
                mViRooster.speakNow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getHour() {
        int hour = -1;
        if (mAlarmObject != null && mViRooster != null) {
            boolean is24Hour = (mAlarmObject.getAlarmIs24Hour() == 1);
            hour = mViRooster.getHourOfDay();
            if (!is24Hour || hour <= 12) {
                hour -= 12;
            }
        }
        return hour;
    }
}
