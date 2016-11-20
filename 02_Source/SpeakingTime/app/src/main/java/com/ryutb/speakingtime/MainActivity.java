package com.ryutb.speakingtime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.view.SpeakingClockTimePicker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyClock";

    private Button mBtnSetAlarm;
    private SpeakingClockTimePicker mTimePicker;
    private CheckBox m24HFormat;
    AlarmObject mAlarmObject;
    private AlarmSettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mBtnSetAlarm = (Button) findViewById(R.id.set_timer);
        mAlarmObject = new AlarmObject(getSharedPreferences(getPackageName(), Context.MODE_PRIVATE));

        Log.d(TAG, ">>>onCreate alarmId= " + mAlarmObject.getAlarmId());
        //mBtnSetAlarm.setText("Set alarm for alarmId= " + String.valueOf(mAlarmObject.getAlarmId()));

        mTimePicker = (SpeakingClockTimePicker) findViewById(R.id.time_picker);
        m24HFormat = (CheckBox) findViewById(R.id.cb_24hour_format);
        m24HFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is42H) {
                mTimePicker.setIs24HourView(is42H);
            }
        });

        mSettingFragment = new AlarmSettingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.setting_alarm, mSettingFragment).commit();

    }


    public void speakHour(View v) throws IOException, ParseException {

        Calendar calendar = GregorianCalendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        int userHour = currentHour;
        if (Build.VERSION.SDK_INT < 23) {
            userHour = mTimePicker.getCurrentHour();
        } else {
            userHour = mTimePicker.getHour();
        }

        int userMinute = currentMinute;
        if (Build.VERSION.SDK_INT < 23) {
            userMinute = mTimePicker.getCurrentMinute();
        } else {
            userMinute = mTimePicker.getMinute();
        }

        Log.d(TAG, ">>>nextTime userHour= " + userHour
                + ", userMinute=" + userMinute);
        if (userHour < currentHour || ((userHour == currentHour) && userMinute < currentMinute)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, userHour);
        calendar.set(Calendar.MINUTE, userMinute);
        calendar.set(Calendar.SECOND, 0);

        String timer = ">>>nextTime d= " + calendar.get(Calendar.DAY_OF_YEAR)
                + ", m= " + calendar.get(Calendar.MONTH)
                + ", y= " + calendar.get(Calendar.YEAR)
                + ", h= " + calendar.get(Calendar.HOUR_OF_DAY)
                + ", m= " + calendar.get(Calendar.MINUTE);
        Toast.makeText(this, timer, Toast.LENGTH_SHORT).show();
        Log.d(TAG, timer);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent recvIntent = new Intent(getApplicationContext(), AlarmActivity.class);
        recvIntent.putExtra(Define.EXTRA_START_FROM_ALARM_MANAGER, true);
        recvIntent.putExtra(Define.EXTRA_ALARM_ID, mAlarmObject.getAlarmId());
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), mAlarmObject.getAlarmId(), recvIntent, 0);
        if (Build.VERSION.SDK_INT >= 19) {
            Log.d(TAG, ">>>speakHour START");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            mAlarmObject.createAlarm();
            int alarmVolume = mSettingFragment.getVolume();
            mAlarmObject.setVolume(alarmVolume);
        }
    }
}
