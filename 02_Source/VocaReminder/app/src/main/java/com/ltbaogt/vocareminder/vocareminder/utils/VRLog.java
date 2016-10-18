package com.ltbaogt.vocareminder.vocareminder.utils;

/**
 * Created by MyPC on 16/10/2016.
 */
public class VRLog {
    public static boolean isLogable = false;

    public static void d(String tag, String msg) {
        if (isLogable) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void e(String tag, String erroMsg) {
        if (isLogable) {
            android.util.Log.e(tag, erroMsg);
        }
    }

    public static void e(String tag, Exception e) {
        if (isLogable) {
            android.util.Log.e(tag, android.util.Log.getStackTraceString(e));
        }
    }
}
