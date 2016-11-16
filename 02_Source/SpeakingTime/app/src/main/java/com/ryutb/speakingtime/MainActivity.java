package com.ryutb.speakingtime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyClock";
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);

        AlarmSettingFragment settingFragment = new AlarmSettingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.setting_alarm, settingFragment).commit();

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
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), Define.REQ_CODE_SET_ARLAM, recvIntent, 0);
        if (Build.VERSION.SDK_INT >= 19) {
            Log.d(TAG, ">>>speakHour START");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
