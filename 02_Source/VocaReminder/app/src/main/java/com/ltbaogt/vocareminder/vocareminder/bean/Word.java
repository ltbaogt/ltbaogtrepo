package com.ltbaogt.vocareminder.vocareminder.bean;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by My PC on 05/08/2016.
 */

public class Word implements Parcelable {

    private int mWordId;
    private String mWordName;
    private String mPronunciation;
    private int mType_ID;
    private String mDefault_Meaning;
    private String mSentence;
    private int mPriority;
    private int mCount;
    private int mGroup_ID;
    private boolean mDeleted;

    public Word() {
        mWordId = -1;
        mWordName = "Name";
        mPronunciation = "Pronunciation";
        mType_ID = 1;
        mDefault_Meaning = "Let describe this word";
        mSentence = "Sentence";
        mPriority = 1;
        mCount = 0;
        mGroup_ID = 1;
        mDeleted = false;
    }

    public Word(int id,
                String name,
                String pronunciation,
                int typeId,
                String default_meaning,
                String sentence,
                int priority,
                int count,
                int groupId,
                boolean deleted) {
        mWordId = id;
        mWordName = name;
        mPronunciation = pronunciation;
        mType_ID = typeId;
        mDefault_Meaning = default_meaning;
        mSentence = sentence;
        mPriority = priority;
        mCount = count;
        mGroup_ID = groupId;
        mDeleted = deleted;
    }

    protected Word(Parcel in) {
        mWordId = in.readInt();
        mWordName = in.readString();
        mPronunciation = in.readString();
        mType_ID = in.readInt();
        mDefault_Meaning = in.readString();
        mSentence = in.readString();
        mPriority = in.readInt();
        mCount = in.readInt();
        mGroup_ID = in.readInt();
        mDeleted = in.readByte() != 0;
    }

    //TODO: What is it?
    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public void setWordId(int id) {
        if (mWordId == -1) {
            mWordId = id;
        } else {
            throw new SQLiteConstraintException("You cannot assign new id");
        }
    }

    public int getWordId() {
        return mWordId;
    }
    public String getWordName() {
        return mWordName;
    }

    public void setWordName(String WordName) {
        this.mWordName = WordName;
    }

    public String getPronunciation() {
        return mPronunciation;
    }

    public void setPronunciation(String Pronunciation) {
        this.mPronunciation = Pronunciation;
    }

    public int getType_ID() {
        return mType_ID;
    }

    public void setType_ID(int mType_ID) {
        this.mType_ID = mType_ID;
    }

    public String getDefault_Meaning() {
        return mDefault_Meaning;
    }

    public void setDefault_Meaning(String Default_Meaning) {
        this.mDefault_Meaning = Default_Meaning;
    }

    public String getSentence() {
        return mSentence;
    }

    public void setSentence(String Sentence) {
        this.mSentence = Sentence;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int Priority) {
        this.mPriority = Priority;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int Count) {
        this.mCount = Count;
    }

    public int getGroup_ID() {
        return mGroup_ID;
    }

    public void setGroup_ID(int Group_ID) {
        this.mGroup_ID = Group_ID;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public void setDeleted(boolean Deleted) {
        this.mDeleted = Deleted;
    }

    @Override
    public String toString() {
        String str = "Word={"
                + " ID= " + getWordId()
                + ", Name= " + getWordName()
                + ", Pronun= " + getPronunciation()
                + ", TypeId= " + getType_ID()
                + ", Meaning= " + getDefault_Meaning()
                + ", Sentence= " + getSentence()
                + ", Priority= " + getPriority()
                + ", Count= " + getCount()
                + ", GroupId= " + getGroup_ID()
                + ", isDeleted= " + isDeleted()
                + " }";
        return str;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(getWordId());
        dest.writeString(getWordName());
        dest.writeString(getPronunciation());
        dest.writeInt(getType_ID());
        dest.writeString(getDefault_Meaning());
        dest.writeString(getSentence());
        dest.writeInt(getPriority());
        dest.writeInt(getCount());
        dest.writeInt(getGroup_ID());
        dest.writeInt(isDeleted() ? 1 : 0);
    }
}
