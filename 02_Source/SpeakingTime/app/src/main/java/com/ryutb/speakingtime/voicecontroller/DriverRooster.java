package com.ryutb.speakingtime.voicecontroller;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.util.Define;

import java.io.IOException;

/**
 * Created by MyPC on 24/11/2016.
 */
public class DriverRooster extends ViRooster {

    private static final String TAG = Define.createTAG("DriverRooster");

    public DriverRooster(Context ctx, int volume, int alarmType) {
        super(ctx, volume, alarmType);
    }

    @Override
    protected void setupVolumeForAudioStream() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(getAudioStream(), audioManager.getStreamMaxVolume(getAudioStream()), 0);
    }
}
