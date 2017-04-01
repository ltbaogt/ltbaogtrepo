package com.ryutb.speakingtime.bean;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.ryutb.speakingtime.util.Define;

/**
 * Created by MyPC on 17/11/2016.
 */
public class AlarmObject {

    private static final String TAG = Define.createTAG("AlarmObject");
    public static String SP_ALARM_ID = "extrea_alarm_id";
    public static String EXTRA_ALARM_ID = SP_ALARM_ID;
    public static String SP_VOLUME = "sp_volume";
    public static String SP_PLAY_ONE_HEADPHONE = "sp_play_on_headphone";
    private SharedPreferences mSharePref;

    public static final String TABLE_NAME = "tbl_Alarm";
    public static final String COL_NAME_ALARM_ID = "Alarm_ID";
    public static final String COL_NAME_ALARM_HOUR = "Alarm_Hour";
    public static final String COL_NAME_ALARM_MINUTE = "Alarm_Minute";
    public static final String COL_NAME_ALARM_VOLUME = "Alarm_Volume";
    public static final String COL_NAME_ALARM_REPEAT = "Alarm_Repeat";
    public static final String COL_NAME_ALARM_TYPE = "Alarm_Type";
    public static final String COL_NAME_ALARM_IS_24_HOUR = "Alarm_Is24Hour";


    private int mAlarmId = -1;
    private int mAlarmHour;
    private int mAlarmMinute;
    private int mAlarmVolume;
    private int mAlarmRepeat = 3;
    private int mAlarmType;
    private int mAlarmIs24Hour;
    private int mInterval;

    public int getInterval() {
        mInterval = 10;
        return mInterval;
    }
    public int getAlarmId() {
        return mAlarmId;
    }

    public void setAlarmId(int mAlarmId) {
        this.mAlarmId = mAlarmId;
    }

    public int getAlarmHour() {
        return mAlarmHour;
    }

    public void setAlarmHour(int p) {
        this.mAlarmHour = p;
    }

    public int getAlarmMinute() {
        return mAlarmMinute;
    }

    public void setAlarmMinute(int p) {
        this.mAlarmMinute = p;
    }

    public int getAlarmVolume() {
        return mAlarmVolume;
    }

    public void setAlarmVolume(int p) {
        this.mAlarmVolume = p;
    }

    public int getAlarmRepeat() {
        return mAlarmRepeat;
    }

    public void setAlarmRepeat(int p) {
        this.mAlarmRepeat = p;
    }

    public int getAlarmType() {
        return mAlarmType;
    }

    public void setAlarmType(int p) {
        this.mAlarmType = p;
    }

    public int getAlarmIs24Hour() {
        return mAlarmIs24Hour;
    }

    public void setAlarmIs24Hour(int p) {
        this.mAlarmIs24Hour = p;
    }

    public AlarmObject() {
    }

    public AlarmObject(int alarmId) {
        this.setAlarmId(alarmId);
    }

    public void setIsPlayOnHeadPhone(boolean isPlayOnHeadPhone) {
        Log.d(TAG, ">>>setIsPlayOnHeadPhone isPlayOnHeadPhone=" + isPlayOnHeadPhone);
        mSharePref.edit().putBoolean(SP_PLAY_ONE_HEADPHONE, isPlayOnHeadPhone).apply();
    }

    public boolean getIsPlayOnHeadPhone() {
        boolean isPlay = mSharePref.getBoolean(SP_PLAY_ONE_HEADPHONE, true);
        Log.d(TAG, ">>>getIsPlayOnHeadPhone isPlay=" + isPlay);
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
        Log.d(TAG, ">>>saveInt key= " + key + ", value= " + intValue);
    }

    private int getInt(String spKey) {
        String key = getAlarmSPFormat(spKey);
        int ret = mSharePref.getInt(key, 0);
        Log.d(TAG, ">>>getInt key= " + key + " value= " + ret);
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

    public void initFromCursor(Cursor c) {
        setAlarmId(c.getInt(0));
        setAlarmHour(c.getInt(1));
        setAlarmMinute(c.getInt(2));
        setAlarmVolume(c.getInt(3));
        setAlarmRepeat(c.getInt(4));
        setAlarmType(c.getInt(5));
        setAlarmIs24Hour(c.getInt(6));
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(AlarmObject.COL_NAME_ALARM_ID, getAlarmId());
        cv.put(AlarmObject.COL_NAME_ALARM_HOUR, getAlarmHour());
        cv.put(AlarmObject.COL_NAME_ALARM_MINUTE, getAlarmMinute());
        cv.put(AlarmObject.COL_NAME_ALARM_REPEAT, getAlarmRepeat());
        cv.put(AlarmObject.COL_NAME_ALARM_VOLUME, getAlarmVolume());
        cv.put(AlarmObject.COL_NAME_ALARM_TYPE, getAlarmType());
        cv.put(AlarmObject.COL_NAME_ALARM_IS_24_HOUR, getAlarmIs24Hour());
        return cv;
    }

    @Override
    public String toString() {
        return "AlarmId= {"
                + "Id= " + getAlarmId()
                + ",Hour= " + getAlarmHour()
                + ",Minute= " + getAlarmMinute()
                + ",Volume= " + getAlarmVolume()
                + ",Repeat= " + getAlarmRepeat()
                + ",Type= " + getAlarmType()
                + ",is24Hour= " + getAlarmIs24Hour()
                + "}";
    }
}
