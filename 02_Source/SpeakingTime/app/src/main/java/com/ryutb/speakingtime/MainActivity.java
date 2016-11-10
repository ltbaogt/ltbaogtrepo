package com.ryutb.speakingtime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
        mTimePicker.setIs24HourView(true);
    }


    public void speakHour(View v) throws IOException, ParseException {
//        Calendar calendar = GregorianCalendar.getInstance();
//        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//        final int minus = calendar.get(Calendar.MINUTE);
//        Uri mp3 = Uri.parse("android.resource://"
//                + getPackageName() + "/raw/"
//                + "h_" + hourOfDay);
//        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), mp3);
//        mediaPlayer.start();
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer2) {
//
//                Uri mp32 = Uri.parse("android.resource://"
//                        + getPackageName() + "/raw/"
//                        + "minute_" + minus);
//                mediaPlayer2 = MediaPlayer.create(getApplicationContext(), mp32);
//                if (mediaPlayer2 != null) {
//                    mediaPlayer2.start();
//                }
//            }
//        });

        Calendar calendar = GregorianCalendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMunite = calendar.get(Calendar.MINUTE);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
//        String nextTimerInString = String.format("%1$d/$2$d/$3$d $4$d:$4$d:$4$d"
//                , calendar.get(Calendar.DAY_OF_MONTH)
//                , calendar.get(Calendar.MONTH)
//        , calendar.get(Calendar.YEAR)
//        , calendar.get(Calendar.HOUR_OF_DAY)
//        , calendar.get(Calendar.MINUTE)
//        , calendar.get(Calendar.SECOND));
//        Date nextTimer = sdf.parse(nextTimerInString);
        int userHour = currentHour;
        if (Build.VERSION.SDK_INT < 23) {
            userHour = mTimePicker.getCurrentHour();
        } else {
            userHour = mTimePicker.getHour();
        }

        int userMinute = currentMunite;
        if (Build.VERSION.SDK_INT < 23) {
            userMinute = mTimePicker.getCurrentMinute();
        } else {
            userMinute = mTimePicker.getMinute();
        }

//        if (currentHour + userHour > 23) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
        calendar.set(Calendar.HOUR_OF_DAY, userHour);
        calendar.set(Calendar.MINUTE, userMinute);
        calendar.set(Calendar.SECOND, 0);
//
//        Log.d(TAG, ">>>nextTime userHour= " + userHour
//                + ", userMinute= " + userMinute);
//        if (userHour > currentHour) {
//            calendar.add(Calendar.HOUR_OF_DAY, (24 - userHour) + userHour);
//            if (userMinute > currentMunite) {
//                calendar.add(Calendar.MINUTE, userMinute - currentMunite);
//            }
//        } else if (userMinute > currentMunite) {
//            calendar.add(Calendar.MINUTE, userMinute - currentMunite);
//        }
//
        String timer = ">>>nextTime d= " + calendar.get(Calendar.DAY_OF_YEAR)
                + ", m= " + calendar.get(Calendar.MONTH)
                + ", y= " + calendar.get(Calendar.YEAR)
                + ", h= " + calendar.get(Calendar.HOUR_OF_DAY)
                + ", m= " + calendar.get(Calendar.MINUTE);
        Toast.makeText(this, timer, Toast.LENGTH_SHORT).show();
        Log.d(TAG, timer);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent recvIntent = new Intent(getApplicationContext(), TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, recvIntent, 0);
        if (Build.VERSION.SDK_INT >= 19) {
            Log.d(TAG, ">>>onReceive START");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }



    }
}
