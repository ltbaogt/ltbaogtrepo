package com.ryutb.speakingtime.receiver;

import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.util.Define;
import com.ryutb.speakingtime.voicecontroller.DriverRooster;
import com.ryutb.speakingtime.voicecontroller.Rooster;
import com.ryutb.speakingtime.voicecontroller.ViRooster;

import java.io.IOException;

/**
 * Created by MyPC on 23/11/2016.
 */
public class VoiceClockBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = Define.createTAG("VoiceClockBroadcastReceiver");

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>>onReceive");

        boolean isHeadphonePlugged = checkWiredHeadphoneIsPlugged(context);
        AlarmObject alarmObject = new AlarmObject(context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE));
        boolean isPlayOnHeadphone = alarmObject.getIsPlayOnHeadPhone();
        Log.d(TAG, ">>>onReceive isHeadphonePlugged= " + isHeadphonePlugged);

        //If play on headphone is uncheck
        if (!isPlayOnHeadphone) {
            speakNow(context, AudioManager.STREAM_ALARM);
        } else if (isPlayOnHeadphone && isHeadphonePlugged) { //If play on headphone is check
            speakNow(context, AudioManager.STREAM_VOICE_CALL);
        }
    }

    private void speakNow(final Context ctx, int streamType) {
        ViRooster timer = new DriverRooster(ctx, 15);
        timer.setAudioStream(streamType);
        try {
            sendBroadcastPauseMusic(ctx);
            timer.speakNow();
            timer.setOnSpeakCompleted(new Rooster.OnSpeakCompleted() {
                @Override
                public void onSpeakCompleted() {
                    sendBroadcastResumeMusic(ctx);
                }

                @Override
                public void onRepeateCompleted() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkWiredHeadphoneIsPlugged(Context ctx) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        Intent headphoneStateIntent = ctx.registerReceiver(null, filter);
        return headphoneStateIntent.getIntExtra("state", 0) == 1;
    }

    private void sendBroadcastPauseMusic(Context ctx) {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        ctx.sendBroadcast(i);
    }

    private void sendBroadcastResumeMusic(Context ctx) {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "play");
        ctx.sendBroadcast(i);
    }
}
