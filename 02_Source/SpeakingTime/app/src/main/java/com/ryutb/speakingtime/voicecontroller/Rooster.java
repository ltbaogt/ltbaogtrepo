package com.ryutb.speakingtime.voicecontroller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.ryutb.speakingtime.activity.AlarmActivity;
import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.util.Define;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public abstract class Rooster {

    private static final String TAG = "MyClock";
    public static final String MINUTE_PREFIX = "minute_";
    public static final String HOUR_PREFIX = "h_";
    public static final int ALARM_LIST_START_INDEX = 0;

    public static final int ALARM_SPEAK_TYPE_NOW_TIME = 0;
    public static final int ALARM_SPEAK_TYPE_TIME = 1;


    ArrayList<MediaPlayer> mMediaPlayers;
    protected Context mContext;
    protected OnSpeakCompleted mOnSpeakCompleted;
    private boolean mIsRepeat = false;
    protected int mRepeatCount;
    private int mRepeatTime;
    private int mDefaultStream = AudioManager.STREAM_ALARM;
    private int mVolumeAlarm;
    private int mAlarmSpeakType;
    boolean mIs24HourVoice = false;

    private AlarmObject mAlarmObject;

    private PendingIntent mNextAlarmPendingIntent;

    public void setIsRepeat(boolean isRepeat) {
        mIsRepeat = isRepeat;
    }

    public boolean getIsRepeat() {
        return mIsRepeat;
    }

    public void setIs24Hour(int is24Hour) {
        mIs24HourVoice = (is24Hour == 1);
    }
    public void setRepeatTime(int time) {
        mRepeatTime = time;
    }

    public int getRepeatTime() {
        mRepeatTime = 20;
        return mRepeatTime;
    }

    public int getVolumeAlarm() {
        return mVolumeAlarm;
    }

    public int getAudioStream() {
        return mDefaultStream;
    }

    public void setAudioStream(int streamType) {
        mDefaultStream = streamType;
    }

    public void cancelRepeat() {
        Log.d(TAG, ">>>cancelRepeat START");
        if (mNextAlarmPendingIntent != null) {
            Log.d(TAG, ">>>cancelRepeat");
            mNextAlarmPendingIntent.cancel();
        }
        mRepeatCount = getRepeatTime();
        mediaplayerStopAndReleaseRemove();
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
            Toast.makeText(mContext, "mMinusSpeakCompleted>>>onCompletion", Toast.LENGTH_SHORT).show();
            if (mOnSpeakCompleted != null) {
                mOnSpeakCompleted.onSpeakCompleted();
            }
            if (getIsRepeat()) {
                doRepeat();
            }
        }
    };

    public Rooster(Context ctx, int volume, int alarmType) {
        mContext = ctx;
        mVolumeAlarm = volume;
        mAlarmSpeakType = alarmType;
        mMediaPlayers = new ArrayList<>();
    }

    public Rooster(Context ctx, AlarmObject alarmObject, int alarmType) {
        mContext = ctx;
        mAlarmObject = alarmObject;
        mMediaPlayers = new ArrayList<>();
        mAlarmSpeakType = alarmType;
    }

    protected abstract int prepareMediaPlayerHour(int hour);

    protected abstract int prepareMediaPlayerMinute(int minute);

    protected void prepareMediaPlayer(int rawResIdHour, int rawResIdMinute) {

        try {
            //Relase old resource
            mediaplayerReleaseAndRemove();

            setupVolumeForAudioStream();

            //Create new media player
            MediaPlayer minuteMediaPlayer = createMP(rawResIdMinute);
            minuteMediaPlayer.prepare();


            final MediaPlayer hourMediaPlayer = createMP(rawResIdHour);
            mMediaPlayers.add(hourMediaPlayer);
            mMediaPlayers.add(minuteMediaPlayer);

            hourMediaPlayer.prepareAsync();
            hourMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayers.get(ALARM_LIST_START_INDEX).start();
                }
            });
            hourMediaPlayer.setNextMediaPlayer(minuteMediaPlayer);

            hourMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.d(TAG, ">>>onError mHourMediaPlayer");
                    return false;
                }
            });

            minuteMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.d(TAG, ">>>onError mMinuteMediaPlayer");
                    return false;
                }
            });
            mMediaPlayers.get(ALARM_LIST_START_INDEX).setOnCompletionListener(mMinusSpeakCompleted);
        } catch (IOException e) {
            Log.e(TAG, "Cannot play time because= " + Log.getStackTraceString(e));
        }
    }

    public void speakNow() throws IOException {

        int hourOfDay = getHourOfDay();
        final int minus = getMinute();

        if (!mIs24HourVoice && hourOfDay > 12) {
            hourOfDay -= 12;
        }
        prepareMP(hourOfDay, minus);

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

    protected void prepareMP(int hour, int minute) {
        int hourRes = prepareMediaPlayerHour(hour);
        int minuteRes = prepareMediaPlayerMinute(minute);
        prepareMediaPlayer(hourRes, minuteRes);
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

    private void mediaplayerReleaseAndRemove() {
        //Relase old resource
        if (mMediaPlayers != null) {

            for (int i = mMediaPlayers.size() - 1; i >= 0; i--) {
                mMediaPlayers.get(i).release();
                mMediaPlayers.remove(i);
            }
        }
    }

    private void mediaplayerStopAndReleaseRemove() {
        //Relase old resource
        if (mMediaPlayers != null) {

            for (int i = mMediaPlayers.size() - 1; i >= 0; i--) {
                mMediaPlayers.get(i).stop();
                mMediaPlayers.get(i).release();
                mMediaPlayers.remove(i);
            }
        }
    }

    protected void setupVolumeForAudioStream() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        int volumeOfAlarm = mAlarmObject == null ? mVolumeAlarm : mAlarmObject.getAlarmVolume();
        audioManager.setStreamVolume(mDefaultStream, volumeOfAlarm, 0);
    }
}
