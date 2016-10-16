package com.ltbaogt.vocareminder.vocareminder.bean;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Parcel;
import android.os.Parcelable;

import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

/**
 * Created by My PC on 05/08/2016.
 */

@SuppressLint("ParcelCreator")
public class Word extends BaseWord implements Parcelable {

    public static final String TABLE_NAME_TBL_WORD = "tbl_Word";
    public static final int COL_WORD_ID_INDEX = 0;
    public static final int COL_WORDNAME_INDEX = 1;
    public static final int COL_PRONUNCIATION_INDEX = 2;
    public static final int COL_TYPE_ID_INDEX = 3;
    public static final int COL_DEFAULT_MEANING_INDEX = 4;
    public static final int COL_SENTENCE_INDEX = 5;
    public static final int COL_PRIORITY_INDEX = 6;
    public static final int COL_COUNT_INDEX = 7;
    public static final int COL_GROUP_ID_INDEX = 8;
    public static final int COL_DELETED_INDEX = 9;
    public static final int COL_POSITION_INDEX = 10;
    public static final int COL_MP3_URL_INDEX = 11;

    public static final String COL_WORD_ID = "Word_ID";
    public static final String COL_WORDNAME = "WordName";
    public static final String COL_PRONUNCIATION = "Pronunciation";
    public static final String COL_TYPE_ID = "Type_ID";
    public static final String COL_DEFAULT_MEANING = "Default_Meaning";
    public static final String COL_SENTENCE = "Sentence";
    public static final String COL_PRIORITY = "Priority";
    public static final String COL_COUNT = "Count";
    public static final String COL_GROUP_ID = "Group_ID";
    public static final String COL_DELETED = "Deleted";
    public static final String COL_POSITION = "Position";
    public static final String COL_MP3_URL = "Mp3Url";
    public static final String WHERE_DELETE = COL_DELETED + "= 0";
    public static final String WHERE_ARCHIVED = COL_DELETED + "= 1";

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
    private String mPosition;
    private String mMp3Url;

    public Word() {
        mWordId = -1;
        mWordName = Define.WORD_INIT_NAME;
        mPronunciation = Define.WORD_INIT_PRONUNCIATION;
        mType_ID = 1;
        mDefault_Meaning = Define.WORD_INIT_DESCRIPTION;
        mSentence = "Sentence";
        mPriority = 1;
        mCount = 0;
        mGroup_ID = 1;
        mDeleted = false;
        mPosition = "";
        mMp3Url = "";
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
                boolean deleted,
                String position,
                String mp3Url) {
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
        mPosition = position;
        mMp3Url = mp3Url;
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
        mPosition = in.readString();
        mMp3Url = in.readString();
    }

//    //TODO: What is it?
//    public static final Creator<Word> CREATOR = new Creator<Word>() {
//        @Override
//        public Word createFromParcel(Parcel in) {
//            return new Word(in);
//        }
//
//        @Override
//        public Word[] newArray(int size) {
//            return new Word[size];
//        }
//    };

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

    @Override
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

    public void setPosition(String pos) {
        mPosition = pos;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setMp3Url(String url) {
        if (VRStringUtil.isStringNullOrEmpty(url)) return;
        url = url.trim();
        mMp3Url = url;
    }

    @Override
    public String getMp3Url() {
        return mMp3Url;
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
                + ", Position= " + getPosition()
                + ", Mp3Url= " + getMp3Url()
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
        dest.writeString(getPosition());
        dest.writeString(getMp3Url());
    }

    public void initFromCursor(Cursor cs) {
        this.setWordId(cs.getInt(COL_WORD_ID_INDEX));
        this.setWordName(cs.getString(COL_WORDNAME_INDEX));
        this.setPronunciation(cs.getString(COL_PRONUNCIATION_INDEX));
        this.setDefault_Meaning(cs.getString(COL_DEFAULT_MEANING_INDEX));
        this.setDeleted((cs.getInt(COL_DELETED_INDEX) == 1));
        this.setPosition(cs.getString(COL_POSITION_INDEX));
        this.setMp3Url(cs.getString(COL_MP3_URL_INDEX));
    }


}
