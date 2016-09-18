package com.ltbaogt.vocareminder.vocareminder.bean;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteConstraintException;

/**
 * Created by MyPC on 17/09/2016.
 */
@SuppressLint("ParcelCreator")
public class ConvertWord {
    //    private int mWordId;
    private int a;
    //    private String mWordName;
    private String b;
    //    private String mPronunciation;
    private String c;
    //    private int mType_ID;
    private int d;
    //    private String mDefault_Meaning;
    private String e;
    //    private String mSentence;
    private String f;
    //    private int mPriority;
    private int g;
    //    private int mCount;
    private int h;
    //    private int mGroup_ID;
    private int i;
    //    private boolean mDeleted;
    private boolean j;
    public ConvertWord(int id,
                       String name,
                       String pronunciation,
                       int typeId,
                       String default_meaning,
                       String sentence,
                       int priority,
                       int count,
                       int groupId,
                       boolean deleted) {
        a = id;
        //    private String mWordName;
        b = name;
        //    private String mPronunciation;
        c = pronunciation;
        //    private int mType_ID;
        d = typeId;
        //    private String mDefault_Meaning;
        e = default_meaning;
        //    private String mSentence;
        f = sentence;
        //    private int mPriority;
        g = priority;
        //    private int mCount;
        h = count;
        //    private int mGroup_ID;
        i = groupId;
        //    private boolean mDeleted;
        j = deleted;
    }
    public int getWordId() {
        return a;
    }
    public String getWordName() {
        return b;
    }
    public String getPronunciation() {
        return c;
    }
    public int getType_ID() {
        return d;
    }
    public String getDefault_Meaning() {
        return e;
    }
    public String getSentence() {
        return f;
    }
    public int getPriority() {
        return g;
    }
    public int getCount() {
        return h;
    }
    public int getGroup_ID() {
        return i;
    }
    public boolean isDeleted() {
        return j;
    }
    public static ConvertWord fromWord(Word w) {
        return new ConvertWord(
                w.getWordId(),
                w.getWordName(),
                w.getPronunciation(),
                w.getType_ID(),
                w.getDefault_Meaning(),
                w.getSentence(),
                w.getPriority(),
                w.getCount(),
                w.getGroup_ID(),
                w.isDeleted()
        );
    }

    public static Word toWord(ConvertWord w) {
        return new Word(
                w.getWordId(),
                w.getWordName(),
                w.getPronunciation(),
                w.getType_ID(),
                w.getDefault_Meaning(),
                w.getSentence(),
                w.getPriority(),
                w.getCount(),
                w.getGroup_ID(),
                w.isDeleted()
        );
    }
}
