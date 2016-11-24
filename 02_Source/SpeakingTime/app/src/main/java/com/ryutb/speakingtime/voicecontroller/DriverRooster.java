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

    public DriverRooster(Context ctx, int volume) {
        super(ctx, volume);
    }

    @Override
    protected MediaPlayer prepareMediaPlayer(int rawResIdHour, int rawResIdMinute) {

        try {
            //Relase old resource
            if (mHourMediaPlayer != null) {
                mHourMediaPlayer.release();
                mHourMediaPlayer = null;
            }

            if (mMinuteMediaPlayer != null) {
                mMinuteMediaPlayer.release();
                mMinuteMediaPlayer = null;
            }

            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

            audioManager.setStreamVolume(getAudioStream(), audioManager.getStreamMaxVolume(getAudioStream()), 0);

            //Create new media player
            mMinuteMediaPlayer = createMP(rawResIdMinute);
            mMinuteMediaPlayer.prepare();

            mHourMediaPlayer = createMP(rawResIdHour);
            mHourMediaPlayer.prepareAsync();
            mHourMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mHourMediaPlayer.start();
                }
            });
            mHourMediaPlayer.setNextMediaPlayer(mMinuteMediaPlayer);

            mHourMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.d(TAG, ">>>onError mHourMediaPlayer");
                    return false;
                }
            });

            mMinuteMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.d(TAG, ">>>onError mMinuteMediaPlayer");
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHourMediaPlayer;
    }
}
