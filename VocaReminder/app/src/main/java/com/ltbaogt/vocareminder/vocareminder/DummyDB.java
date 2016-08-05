package com.ltbaogt.vocareminder.vocareminder;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by My PC on 05/08/2016.
 */

public class DummyDB {

    Context mContext;
    String[] mArrayVoca;
    String[] mArrayStc;

    public DummyDB(Context ctx) {
        this.mContext = ctx;
        mArrayVoca = mContext.getResources().getStringArray(R.array.vocabulary);
        mArrayStc = mContext.getResources().getStringArray(R.array.sentence);
    }

    public Word getWordAt(int id) {
        Word w = new Word();
        w.setName(mArrayVoca[id]);
        w.setSentence(mArrayStc[id]);
        return w;
    }
}
