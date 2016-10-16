package com.ltbaogt.vocareminder.vocareminder.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Setting;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordProvider;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import java.util.ArrayList;

/**
 * Created by MyPC on 10/09/2016.
 */
public class ProviderWrapper {

    private static final String TAG = Define.TAG + "ProviderWrapper";
    private Context mContext;

    public ProviderWrapper(Context ctx) {
        mContext = ctx;
    }

    public ArrayList<Word> getListWord() {
        ArrayList<Word> list = new ArrayList<>();
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/words");
        Cursor words = mContext.getContentResolver().query(uri, WordProvider.PROJECTION_ALL, null, null, null);
        if (words.moveToFirst()) {
            do {
                Word w = new Word();
                w.initFromCursor(words);
                list.add(w);
            } while (words.moveToNext());
        }
        words.close();
        return list;
    }

    public ArrayList<Word> getArchivedListWord() {
        ArrayList<Word> list = new ArrayList<>();
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/archived");
        Cursor words = mContext.getContentResolver().query(uri, WordProvider.PROJECTION_ALL, null, null, null);
        if (words != null && words.moveToFirst()) {
            do {
                Word w = new Word();
                w.initFromCursor(words);
                list.add(w);
            } while (words.moveToNext());
            words.close();
        }

        return list;
    }

    public Word getRandomWord() {
        Word w = new Word();
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/words/" + "5");
        Cursor words = mContext.getContentResolver().query(uri, WordProvider.PROJECTION_ALL, null, null, null);
        if (words != null) {
            if (words.moveToFirst()) {
                w.initFromCursor(words);
            }
            words.close();
        }
        VRLog.d(TAG, ">>>getRandomWord Word ID= " + w.getWordId());
        return w;
    }

    private int getIntValueForKey(String key) {
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/settings");
        Cursor settings = mContext.getContentResolver().query(uri, null, key, null, null);
        int intValue = settings.getInt(Setting.COL_INDEX_VALUE);
        VRLog.d("ProviderWrapper", ">>>getIntValueForKey " + intValue);
        settings.close();
        return intValue;
    }

    private void setIntValueForKey(String key, int intValue) {
        VRLog.d("ProviderWrapper", ">>>setIntValueForKey START");
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/settings");
        ContentValues cv = new ContentValues();
        cv.put(key, intValue);
        mContext.getContentResolver().update(uri, cv, key, null);
        VRLog.d("ProviderWrapper", ">>>setIntValueForKey END");
    }
    public int getColorTheme() {
        int color = getIntValueForKey(Define.THEME_COLOR);
        if (color == -1) {
            color = ContextCompat.getColor(mContext.getApplicationContext(), R.color.teal);
        }
        VRLog.d("ProviderWrapper", ">>>getColorTheme size= " + color);
        return color;
    }

    public void updateColorTheme(int color) {
        setIntValueForKey(Define.THEME_COLOR, color);
    }

    public int getDismissTime() {
        return getIntValueForKey(Define.DISMISS_TIME);
    }

    public void updateDismissTime(int time) {
        setIntValueForKey(Define.DISMISS_TIME, time);
    }

    public int getServiceRunningStatus(){
        return getIntValueForKey(Define.SERVICE_STATUS);
    }

    public void setServiceRunningStatus(int runningState) {
        setIntValueForKey(Define.SERVICE_STATUS, runningState);
    }

}
