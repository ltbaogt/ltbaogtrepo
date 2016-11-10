package com.ryutb.speakingtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by MyPC on 10/11/2016.
 */
public class TimeReceiver extends BroadcastReceiver {

    private static final String TAG = "MyClock";

    Handler mHandler = new Handler();

    Runnable mRunable = new Runnable() {
        @Override
        public void run() {

        }
    };

    int mCount = 0;
    @Override
    public void onReceive(final Context context, Intent intent) {
//        mRunable = new Runnable() {
//            @Override
//            public void run() {
//                speakTime(context);
//                mCount++;
//                if (mCount < 2) {
//                    mHandler.postDelayed(this, 1000);
//                }
//            }
//        };
//        mHandler.post(mRunable);
        speakTime(context);
        Log.d(TAG, ">>>onReceive END");
    }

    private void speakTime(final Context context) {
        Calendar calendar = GregorianCalendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minus = calendar.get(Calendar.MINUTE);
        Uri mp3 = Uri.parse("android.resource://"
                + context.getPackageName() + "/raw/"
                + "h_" + hourOfDay);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, mp3);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer2) {

                Uri mp32 = Uri.parse("android.resource://"
                        + context.getPackageName() + "/raw/"
                        + "minute_" + minus);
                mediaPlayer2 = MediaPlayer.create(context, mp32);
                if (mediaPlayer2 != null) {
                    mediaPlayer2.start();
                }
            }
        });
    }
}
