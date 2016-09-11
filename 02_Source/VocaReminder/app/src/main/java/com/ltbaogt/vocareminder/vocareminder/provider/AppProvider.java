package com.ltbaogt.vocareminder.vocareminder.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ltbaogt.vocareminder.vocareminder.bean.WordProvider;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;

/**
 * Created by MyPC on 10/09/2016.
 */
public class AppProvider extends ContentProvider {

    private static final int WORD_LIST = 1;
    private static final int WORD_ID = 2;
    private static final UriMatcher URI_MATCHER;
    private OALDatabaseOpenHelper mDbHepler;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AppContact.AUTHORITY, "words", WORD_LIST);
        URI_MATCHER.addURI(AppContact.AUTHORITY, "words/#", WORD_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHepler = new OALDatabaseOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor c = null;
        if (URI_MATCHER.match(uri) == WORD_LIST) {
            c = mDbHepler.getAllWordsOrderByNameInCursor();
            c.setNotificationUri(getContext().getContentResolver(), uri);
        } else if (URI_MATCHER.match(uri) == WORD_ID){
            c = mDbHepler.randomWordInCursor();
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
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
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
