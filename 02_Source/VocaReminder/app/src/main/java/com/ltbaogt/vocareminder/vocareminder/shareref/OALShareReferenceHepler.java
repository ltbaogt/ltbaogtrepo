package com.ltbaogt.vocareminder.vocareminder.shareref;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by My PC on 07/08/2016.
 */

public class OALShareReferenceHepler {

    public static final String TAG = Define.TAG + "OALShareReferenceHepler";
    Context mContext;
    SharedPreferences mSharedPreferences;
    private static final String MYPREFERENCES = "MyPrefs";
    public static final String THEME_COLOR = "color_theme";

    public OALShareReferenceHepler(Context ctx) {
        mContext = ctx;
        mSharedPreferences = mContext.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor;
    }

    private void putInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    private int getInt(String key, int value) {
        return mSharedPreferences.getInt(key, value);
    }

    public void setThemeColor(int color) {
        putInt(THEME_COLOR, color);
    }

    //Get color of Reminder layout
    public int getThemeColor() {
        int ret = Color.parseColor(Define.DEFAULT_THEME_COLOR);
        try {
            //If first start we will use default theme color
            ret = getInt(THEME_COLOR, ret);
        } catch(NullPointerException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return ret;
    }
}
