package com.ryutb.speakingtime.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ryutb.speakingtime.util.Define;

/**
 * Created by MyPC on 17/11/2016.
 */
public class AlarmObject {

    private static final String TAG = Define.createTAG("AlarmObject");
    public static String SP_ALARM_ID = "extrea_alarm_id";
    public static String EXTRA_ALARM_ID = SP_ALARM_ID;
    public static String SP_TIME_HOUR = "sp_time_hours";
    public static String SP_TIME_MINUTE = "sp_time_minute";
    public static String SP_VOLUME = "sp_volume";
    public static String SP_PLAY_ONE_HEADPHONE = "sp_play_on_headphone";
    private SharedPreferences mSharePref;
    private String mAlarmId;

    private AlarmObject() {

    }

    public AlarmObject(Context ctx, int alarmId) {
        mSharePref = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        this.setAlarmId(alarmId);
    }

    public AlarmObject(SharedPreferences sp) {
        this.mSharePref = sp;
        this.setAlarmId(generateAlarmId());
    }

    public void setAlarmId(int alarmId) {
        this.mAlarmId = String.valueOf(alarmId);
    }


    public int getAlarmId() {
        return Integer.valueOf(mAlarmId);
    }

    public void setAlarmTimeHour(int hour) {
        saveInt(SP_TIME_HOUR, hour);
    }

    public void getAlarmTimeHour() {
        getInt(SP_TIME_HOUR);
    }

    public void setAlarmTimeMinute(int minute) {
        saveInt(SP_TIME_MINUTE, minute);
    }

    public void getAlarmTimeMinute() {
        getInt(SP_TIME_MINUTE);
    }

    public void setVolume(int value) {
        saveInt(SP_VOLUME, value);
    }

    public int getVolume() {
        return getInt(SP_VOLUME);
    }

    public void setIsPlayOnHeadPhone(boolean isPlayOnHeadPhone) {
        Log.d(TAG, ">>>setIsPlayOnHeadPhone isPlayOnHeadPhone=" +isPlayOnHeadPhone);
        mSharePref.edit().putBoolean(SP_PLAY_ONE_HEADPHONE, isPlayOnHeadPhone).apply();
    }

    public boolean getIsPlayOnHeadPhone() {
        boolean isPlay = mSharePref.getBoolean(SP_PLAY_ONE_HEADPHONE, true);
        Log.d(TAG, ">>>getIsPlayOnHeadPhone isPlay=" +isPlay);
        return isPlay;
    }
    public void createAlarm() {
        saveCurrentAlarmId(this.getAlarmId());
    }

    private String getAlarmSPFormat(String spKey) {
        return String.valueOf(this.mAlarmId) + "." + spKey;
    }

    private void saveInt(String spKey, int intValue) {
        String key = getAlarmSPFormat(spKey);
        mSharePref.edit().putInt(key, intValue).apply();
        Log.d(TAG,">>>saveInt key= " + key + ", value= " + intValue);
    }

    private int getInt(String spKey) {
        String key = getAlarmSPFormat(spKey);
        int ret = mSharePref.getInt(key, 0);
        Log.d(TAG,">>>getInt key= " + key + " value= " + ret);
        return ret;
    }

    private void saveCurrentAlarmId(int intValue) {
        mSharePref.edit().putInt(SP_ALARM_ID, intValue).apply();
    }

    private int getCurrentAlarmId() {
        return mSharePref.getInt(SP_ALARM_ID, 0);
    }

    public int generateAlarmId() {
        return getCurrentAlarmId() + 1;
    }

}
