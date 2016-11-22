package com.ryutb.speakingtime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import com.ryutb.speakingtime.bean.AlarmObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public abstract class Rooster {

    private static final String TAG = "MyClock";
    public static final String MINUTE_PREFIX = "minute_";
    public static final String HOUR_PREFIX = "h_";


    protected MediaPlayer mHourMediaPlayer;
    protected MediaPlayer mMinuteMediaPlayer;
    protected Context mContext;
    protected OnSpeakCompleted mOnSpeakCompleted;
    private boolean mIsRepeat;
    protected int mRepeatCount;
    private int mRepeatTime;
    private int mDefaultStream = AudioManager.STREAM_ALARM;

    private AlarmObject mAlarmObject;

    private PendingIntent mNextAlarmPendingIntent;

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
        Log.d(TAG, ">>>cancelRepeat START");
        if (mNextAlarmPendingIntent != null) {
            Log.d(TAG, ">>>cancelRepeat");
            mNextAlarmPendingIntent.cancel();
        }
        mRepeatCount = getRepeatTime();
        if (mHourMediaPlayer != null) {
            mHourMediaPlayer.stop();
            mHourMediaPlayer.release();
            mHourMediaPlayer = null;
        }

        if (mMinuteMediaPlayer != null) {
            mMinuteMediaPlayer.stop();
            mMinuteMediaPlayer.release();
            mMinuteMediaPlayer = null;
        }
        Log.d(TAG, ">>>cancelRepeat END");
    }

    public void setOnSpeakCompleted(OnSpeakCompleted l) {
        mOnSpeakCompleted = l;
    }

    public interface OnSpeakCompleted {
        void onSpeakCompleted();

        void onRepeateCompleted();
    }

    //Minute speak completed listener
    MediaPlayer.OnCompletionListener mMinusSpeakCompleted = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mOnSpeakCompleted != null) {
                mOnSpeakCompleted.onSpeakCompleted();
            }
            if (getIsRepeat()) {
                doRepeat();
            }
        }
    };

    public Rooster(Context ctx, AlarmObject alarmObject) {
        mContext = ctx;
        mAlarmObject = alarmObject;
    }

    protected abstract int prepareMediaPlayerHour(int hour);

    protected abstract int prepareMediaPlayerMinute(int minute);

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
            audioManager.setStreamVolume(mDefaultStream, mAlarmObject.getVolume(), 0);

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

    public void speakNow() throws IOException {

        int hourOfDay = getHourOfDay();
        final int minus = getMinute();

        MediaPlayer mediaPlayer = prepareMP(hourOfDay, minus);
        mMinuteMediaPlayer.setOnCompletionListener(mMinusSpeakCompleted);
        mediaPlayer.start();
    }


    protected MediaPlayer createMP(int restId) {

        MediaPlayer mediaPlayer = null;
        try {
            AssetFileDescriptor afd = mContext.getResources()
                    .openRawResourceFd(restId);

            mediaPlayer = new MediaPlayer();
            //STREAM_ALARM: music is mute, alarm sound is fired
            mediaPlayer.setAudioStreamType(mDefaultStream);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    protected MediaPlayer prepareMP(int hour, int minute) {
        int hourRes = prepareMediaPlayerHour(hour);
        int minuteRes = prepareMediaPlayerMinute(minute);
        return prepareMediaPlayer(hourRes, minuteRes);
    }

    protected void doRepeat() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getHourOfDay());
        calendar.set(Calendar.MINUTE, getMinute() + 1);
        calendar.set(Calendar.SECOND, 0);

        String timer = ">>>doRepeat d= " + calendar.get(Calendar.DAY_OF_YEAR)
                + ", m= " + calendar.get(Calendar.MONTH)
                + ", y= " + calendar.get(Calendar.YEAR)
                + ", h= " + calendar.get(Calendar.HOUR_OF_DAY)
                + ", m= " + calendar.get(Calendar.MINUTE);
        Log.d(TAG, timer);

        if (mNextAlarmPendingIntent != null) {
            mNextAlarmPendingIntent.cancel();
        }
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent recvIntent = new Intent(mContext, AlarmActivity.class);
        recvIntent.putExtra(Define.EXTRA_REPEAT_ALARM, true);
        mNextAlarmPendingIntent = PendingIntent.getActivity(mContext, Define.REQ_CODE_REPEATE, recvIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 19) {
            Log.d(TAG, ">>>doRepeat setup alarm");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mNextAlarmPendingIntent);
        }
    }

    protected void SpeakOnRepeat() throws IOException {
        speakNow();
    }

    public int getHourOfDay() {
        Calendar calendar = GregorianCalendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        Calendar calendar = GregorianCalendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond() {
        Calendar calendar = GregorianCalendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }
}
