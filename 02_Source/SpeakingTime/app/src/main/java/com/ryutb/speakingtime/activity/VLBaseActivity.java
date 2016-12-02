package com.ryutb.speakingtime.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.ryutb.speakingtime.util.Define;

/**
 * Created by MyPC on 02/12/2016.
 */
public class VLBaseActivity extends AppCompatActivity {

    public PendingIntent createPendingIntent(int alarmId, int flag) {
        Intent recvIntent = new Intent(getApplicationContext(), AlarmActivity.class);
        recvIntent.setAction(String.valueOf(alarmId));
        recvIntent.putExtra(Define.EXTRA_START_FROM_ALARM_MANAGER, true);
        recvIntent.putExtra(Define.EXTRA_ALARM_ID, alarmId);
        return PendingIntent.getActivity(getApplicationContext(), alarmId, recvIntent, flag);
    }
}
