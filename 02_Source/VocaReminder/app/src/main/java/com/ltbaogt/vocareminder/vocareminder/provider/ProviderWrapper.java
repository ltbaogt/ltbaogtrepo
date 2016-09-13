package com.ltbaogt.vocareminder.vocareminder.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.bean.Setting;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordProvider;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.util.ArrayList;

/**
 * Created by MyPC on 10/09/2016.
 */
public class ProviderWrapper {

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
                w.setWordId(words.getInt(OALDatabaseOpenHelper.COL_WORD_ID_INDEX));
                w.setWordName(words.getString(OALDatabaseOpenHelper.COL_WORDNAME_INDEX));
                w.setPronunciation(words.getString(OALDatabaseOpenHelper.COL_PRONUNCIATION_INDEX));
                w.setDefault_Meaning(words.getString(OALDatabaseOpenHelper.COL_DEFAULT_MEANING_INDEX));
                list.add(w);
            } while (words.moveToNext());
        }
        words.close();
        return list;
    }

    public Word getRandomWord() {
        Word w = new Word();
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/words/" + "5");
        Cursor words = mContext.getContentResolver().query(uri, WordProvider.PROJECTION_ALL, null, null, null);
        if (words.moveToFirst()) {
            w.setWordId(words.getInt(OALDatabaseOpenHelper.COL_WORD_ID_INDEX));
            w.setWordName(words.getString(OALDatabaseOpenHelper.COL_WORDNAME_INDEX));
            w.setPronunciation(words.getString(OALDatabaseOpenHelper.COL_PRONUNCIATION_INDEX));
            w.setDefault_Meaning(words.getString(OALDatabaseOpenHelper.COL_DEFAULT_MEANING_INDEX));
        }
        words.close();
        return w;
    }

    private int getIntValueForKey(String key) {
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/settings");
        Cursor settings = mContext.getContentResolver().query(uri, null, key, null, null);
        int intValue = settings.getInt(Setting.COL_INDEX_VALUE);
        Log.d("ProviderWrapper", ">>>getIntValueForKey " + intValue);
        settings.close();
        return intValue;
    }

    private void setIntValueForKey(String key, int intValue) {
        Log.d("ProviderWrapper", ">>>setIntValueForKey START");
        Uri uri = Uri.parse(AppContact.CONTENT_URI + "/settings");
        ContentValues cv = new ContentValues();
        cv.put(key, intValue);
        mContext.getContentResolver().update(uri, cv, key, null);
        Log.d("ProviderWrapper", ">>>setIntValueForKey END");
    }
    public int getColorTheme() {
        int color = getIntValueForKey(Define.THEME_COLOR);
        Log.d("ProviderWrapper", ">>>getColorTheme size= " + color);
        return color;
    }

    public void updateColorTheme(int color) {
        setIntValueForKey(Define.THEME_COLOR, color);
    }

    public int getDismissTime() {
        int time = getIntValueForKey(Define.DISMISS_TIME);
        return time;
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
