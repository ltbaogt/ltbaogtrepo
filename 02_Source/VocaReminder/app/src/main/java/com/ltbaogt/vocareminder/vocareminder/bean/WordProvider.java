package com.ltbaogt.vocareminder.vocareminder.bean;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.net.Uri;

import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.provider.AppContact;

/**
 * Created by MyPC on 10/09/2016.
 */
@SuppressLint("ParcelCreator")
public class WordProvider extends Word {

    public static final Uri CONTENT_URI =
            Uri.withAppendedPath(AppContact.CONTENT_URI, "words");

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.ltbaogt.vocareminder.app_word";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.ltbaogt.vocareminder.app_word";

    public static final String[] PROJECTION_ALL =
            {OALDatabaseOpenHelper.COL_WORD_ID
            ,OALDatabaseOpenHelper.COL_COUNT
            ,OALDatabaseOpenHelper.COL_DEFAULT_MEANING
            ,OALDatabaseOpenHelper.COL_DELETED
            ,OALDatabaseOpenHelper.COL_GROUP_ID
            ,OALDatabaseOpenHelper.COL_PRIORITY
            ,OALDatabaseOpenHelper.COL_PRONUNCIATION
            ,OALDatabaseOpenHelper.COL_SENTENCE
            ,OALDatabaseOpenHelper.COL_TYPE_ID
            ,OALDatabaseOpenHelper.COL_WORDNAME};
}
