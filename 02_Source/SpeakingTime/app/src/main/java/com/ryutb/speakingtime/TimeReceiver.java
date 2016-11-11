package com.ryutb.speakingtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
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
        ViRooster viRooster = new ViRooster(context);
        viRooster.setOnSpeakCompleted(new Rooster.OnSpeakCompleted() {
            @Override
            public void onSpeakCompleted() {
                Toast.makeText(context, "FINISH", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            viRooster.speakNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, ">>>onReceive END");
    }

}
