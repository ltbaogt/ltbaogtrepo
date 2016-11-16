package com.ryutb.speakingtime;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public class AlarmActivity extends AppCompatActivity {

    public static final String TAG = "MyClock";
    ViRooster mViRooster;
    private boolean mIsRepeate;

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
        setContentView(R.layout.alarm_activity_layout);
        boolean isStart = getIntent().getBooleanExtra(Define.EXTRA_START_FROM_ALARM_MANAGER, false);
        mIsRepeate = getIntent().getBooleanExtra(Define.EXTRA_REPEAT_ALARM, false);

        getIntent().removeExtra(Define.EXTRA_START_FROM_ALARM_MANAGER);
        if (isStart || mIsRepeate) {
            mViRooster = new ViRooster(getApplicationContext());
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
            try {
                mViRooster.speakNow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSpeaking(View v) {
        mViRooster.cancelRepeat();
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
        if (mIsRepeate) {
            try {
                mViRooster.speakNow();
                mIsRepeate = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

}
