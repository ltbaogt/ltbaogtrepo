package com.ryutb.speakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity_layout);
        boolean isStart = getIntent().getBooleanExtra(Define.EXTRA_START_FROM_ALARM_MANAGER, false);
        getIntent().removeExtra(Define.EXTRA_START_FROM_ALARM_MANAGER);
        if (isStart) {
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
        if (mIsRepeate) {
            try {
                mViRooster.speakNow();
                mIsRepeate = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
