package com.ryutb.speakingtime;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public abstract class Rooster {

    private static final String TAG = "MyClock";
    public static final String MINUTE_PREFIX = "minute_";
    public static final String HOUR_PREFIX = "h_";

    protected MediaPlayer mMediaPlayer;
    protected Context mContext;
    protected OnSpeakCompleted mOnSpeakCompleted;
    private boolean mIsRepeat;
    protected int mRepeatCount;
    private int mRepeatTime;

    private Handler mRepeatHandler = new Handler() {


    };
    public void setIsRepeat(boolean isRepeat) {
        mIsRepeat = isRepeat;
    }

    public boolean getIsRepeat() {
        return mIsRepeat;
    }

    public void setRepeatTime(int time) {
        mRepeatTime = time;
    }

    public int getRepeatTime() {
        //return mRepeatTime;
        return 20;
    }

    public void cancelRepeat() {
        mRepeatCount = getRepeatTime();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    public void setOnSpeakCompleted(OnSpeakCompleted l) {
        mOnSpeakCompleted = l;
    }

    public interface OnSpeakCompleted {
        void onSpeakCompleted();
    }

    public Rooster(Context ctx) {
        mContext = ctx;
    }

    public abstract void speakNow() throws IOException;

    protected abstract MediaPlayer prepareMediaPlayerHour(int hour);
    protected abstract MediaPlayer prepareMediaPlayerMinute(int minute);

    protected MediaPlayer prepareMediaPlayer(int rawResId) {

        try {
            mMediaPlayer = new MediaPlayer();
            //STREAM_ALARM: music is mute, alarm sound is fired
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            AssetFileDescriptor afd = mContext.getResources()
                    .openRawResourceFd(rawResId);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mMediaPlayer;
    }

    protected void doRepeat(final MediaPlayer mediaPlayer, int interval) {
        mRepeatCount++;
        Log.d(TAG, ">>>doRepeat mRepeatCount= " + mRepeatCount);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mRepeatCount <= getRepeatTime()) {
                        SpeakOnRepeat();
                    } else {
                        mRepeatCount = 0;
                        mediaPlayer.stop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, interval);

    }

    protected void SpeakOnRepeat() throws IOException {
        speakNow();
    }
}
