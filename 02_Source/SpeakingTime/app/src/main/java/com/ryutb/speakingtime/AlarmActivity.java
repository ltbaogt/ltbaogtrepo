package com.ryutb.speakingtime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public class AlarmActivity extends AppCompatActivity {

    ViRooster mViRooster;
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
                    Toast.makeText(getApplicationContext(), "onSpeakCompleted", Toast.LENGTH_SHORT).show();
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

}
