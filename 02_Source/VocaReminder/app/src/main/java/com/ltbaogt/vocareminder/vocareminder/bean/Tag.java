package com.ltbaogt.vocareminder.vocareminder.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MyPC on 06/09/2016.
 */
public class Tag {

    private int mTagID;
    private String mTagName;

    public int getTagID() {
        return mTagID;
    }

    public void setTagID(int mTagID) {
        this.mTagID = mTagID;
    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String mTagName) {
        this.mTagName = mTagName;
    }
}
