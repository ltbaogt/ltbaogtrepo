package com.ryutb.speakingtime.util;

/**
 * Created by MyPC on 11/11/2016.
 */
public class Define {

    public static final String APP_TAG = "SpeakingClock";
    public static final String EXTRA_START_FROM_ALARM_MANAGER = "start_from_alarm_manager";
    public static final String EXTRA_ALARM_ID = "alarm_id";
    public static final String EXTRA_REPEAT_ALARM = "repeat_alarm";
    public static final String EXTRA_REPEAT_ALARM_COUNT = "repeat_alarm_count";
    public static final int REQ_CODE_SET_ARLAM = 0;
    public static final int REQ_CODE_REPEATE = 1;

    public static String createTAG(String className) {
        return APP_TAG + "." + className;
    }
}
