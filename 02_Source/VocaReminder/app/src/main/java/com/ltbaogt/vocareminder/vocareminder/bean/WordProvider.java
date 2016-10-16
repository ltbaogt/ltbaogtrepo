package com.ltbaogt.vocareminder.vocareminder.bean;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.net.Uri;

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
            {Word.COL_WORD_ID
            ,Word.COL_COUNT
            ,Word.COL_DEFAULT_MEANING
            ,Word.COL_DELETED
            ,Word.COL_GROUP_ID
            ,Word.COL_PRIORITY
            ,Word.COL_PRONUNCIATION
            ,Word.COL_SENTENCE
            ,Word.COL_TYPE_ID
            ,Word.COL_WORDNAME};
}
