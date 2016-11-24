package com.ryutb.speakingtime.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ryutb.speakingtime.util.Define;
import com.ryutb.speakingtime.R;
import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.fragment.AlarmSettingFragment;
import com.ryutb.speakingtime.view.SpeakingClockTimePicker;

import java.util.Calendar;
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


    public void speakHour(View v) {
        setTimer();
    }

    private void setTimer() {
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
        calendar.set(Calendar.MILLISECOND, 0);

//        String timer = ">>>nextTime d= " + calendar.get(Calendar.DAY_OF_YEAR)
//                + ", m= " + calendar.get(Calendar.MONTH)
//                + ", y= " + calendar.get(Calendar.YEAR)
//                + ", h= " + calendar.get(Calendar.HOUR_OF_DAY)
//                + ", m= " + calendar.get(Calendar.MINUTE);

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
            String alarmString = getAlarmInFurture(calendar);
            Toast.makeText(getApplicationContext(), alarmString, Toast.LENGTH_LONG).show();
        }

        backToPreviousScreen(null);
    }

    private String getAlarmInFurture(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        long minute = (calendar.getTimeInMillis() - currentCalendar.getTimeInMillis()) / 1000 / 60;
        long hours = minute / 60;
        long minute2 = minute - (hours * 60);
        String ret = "Báo thức sau ";
        if (hours > 0) {
            if (hours == 1) {
                ret += String.valueOf(hours) + " giờ ";
            } else {
                ret += String.valueOf(hours) + " giờs ";
            }
        }
        if (minute2 > 0) {
            if (minute2 == 1) {
                ret += String.valueOf(minute2) + " phút";
            } else {
                ret += String.valueOf(minute2) + " phúts";
            }
        }

        if (hours == 0 && minute == 0) {
            ret = "Báo thức ngay bây giờ";
        }
        return ret;
    }
    public void backToPreviousScreen(View v) {
        finish();
    }
}
