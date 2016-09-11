package com.ltbaogt.vocareminder.vocareminder.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordProvider;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;

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
            do{
                Word w = new Word();
                w.setWordId(words.getInt(OALDatabaseOpenHelper.COL_WORD_ID_INDEX));
                w.setWordName(words.getString(OALDatabaseOpenHelper.COL_WORDNAME_INDEX));
                w.setPronunciation(words.getString(OALDatabaseOpenHelper.COL_PRONUNCIATION_INDEX));
                w.setDefault_Meaning(words.getString(OALDatabaseOpenHelper.COL_DEFAULT_MEANING_INDEX));
                list.add(w);
            } while(words.moveToNext());
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
}
