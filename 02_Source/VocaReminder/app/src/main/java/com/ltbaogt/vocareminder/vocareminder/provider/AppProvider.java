package com.ltbaogt.vocareminder.vocareminder.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.bean.WordProvider;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 10/09/2016.
 */
public class AppProvider extends ContentProvider {

    private static final String TAG = Define.TAG + "AppProvider";
    private static final int WORD_LIST = 1;
    private static final int WORD_ID = 2;
    private static final int SETTING_LIST = 3;
    private static final int ARCHIVED_LIST = 4;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AppContact.AUTHORITY, "words", WORD_LIST);
        URI_MATCHER.addURI(AppContact.AUTHORITY, "archived", ARCHIVED_LIST);
        URI_MATCHER.addURI(AppContact.AUTHORITY, "words/#", WORD_ID);
        URI_MATCHER.addURI(AppContact.AUTHORITY, "settings", SETTING_LIST);
    }


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String selection, String[] strings1, String s1) {
        Cursor c = null;
        OALDatabaseOpenHelper dbHepler = new OALDatabaseOpenHelper(getContext());
        if (URI_MATCHER.match(uri) == WORD_LIST) {
            c = dbHepler.getAllWordsOrderByNameInCursor();
        } else if (URI_MATCHER.match(uri) == ARCHIVED_LIST) {
            c = dbHepler.getArchivedWordsOrderByNameInCursor();
        } else if (URI_MATCHER.match(uri) == WORD_ID) {
            c = dbHepler.randomWordInCursor();
        } else if (URI_MATCHER.match(uri) == SETTING_LIST) {
            Log.d(TAG, ">>>query SETTING_LIST");
            c = dbHepler.getSettingValueForKey(selection);
        }
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        dbHepler.close();
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case WORD_LIST:
                return WordProvider.CONTENT_TYPE;
            case WORD_ID:
                return WordProvider.CONTENT_ITEM_TYPE;
            case SETTING_LIST:
                return WordProvider.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String key, String[] strings) {
        OALDatabaseOpenHelper dbHepler = new OALDatabaseOpenHelper(getContext());
        if (URI_MATCHER.match(uri) == SETTING_LIST) {
            Log.d(TAG, ">>>update START");
            int color = contentValues.getAsInteger(key);
            dbHepler.setSettingValueForKey(key, String.valueOf(color));
            Log.d(TAG, ">>>update END");
        }
        dbHepler.close();
        return 0;
    }


}
