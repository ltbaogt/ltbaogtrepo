package com.ryutb.speakingtime.bean;

import android.content.SharedPreferences;

/**
 * Created by MyPC on 17/11/2016.
 */
public class AlarmObject {

    public static String SP_ALARM_ID = "extrea_alarm_id";
    public static String EXTRA_ALARM_ID = SP_ALARM_ID;
    public static String SP_TIME_HOUR = "sp_time_hours";
    public static String SP_TIME_MINUTE = "sp_time_minute";
    private SharedPreferences mSharePref;
    private String mAlarmId;

    private AlarmObject() {

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

    public void createAlarm() {
        saveCurrentAlarmId(this.getAlarmId());
    }
    private String getAlarmSPFormat(String spKey) {
        return String.valueOf(this.mAlarmId) + spKey;
    }

    private void saveInt(String spKey, int intValue) {
        mSharePref.edit().putInt(getAlarmSPFormat(spKey), intValue).commit();
    }

    private int getInt(String spKey) {
        return mSharePref.getInt(getAlarmSPFormat(spKey), 0);
    }

    private void saveCurrentAlarmId(int intValue) {
        mSharePref.edit().putInt(SP_ALARM_ID, intValue).commit();
    }

    private int getCurrentAlarmId() {
        return mSharePref.getInt(SP_ALARM_ID, 0);
    }
    public int generateAlarmId() {
        return getCurrentAlarmId() + 1;
    }

}
