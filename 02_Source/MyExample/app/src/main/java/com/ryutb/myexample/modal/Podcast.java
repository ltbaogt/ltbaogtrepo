package com.ryutb.myexample.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MyPC on 25/02/2017.
 */
public class Podcast implements Parcelable {

    private int mIndex;
    private String mPodcastName;
    private String mPodcastURI;

    public Podcast() {

    }

    public Podcast(Parcel in) {
        mIndex = in.readInt();
        mPodcastName = in.readString();
        mPodcastURI = in.readString();
    }

    public static final Creator<Podcast> CREATOR = new Creator<Podcast>() {
        @Override
        public Podcast createFromParcel(Parcel in) {
            return new Podcast(in);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }
    public String getPodcastPath() {
        return mPodcastURI;
    }

    public void setPodcastPath(String p) {
        this.mPodcastURI = p;
    }

    public String getPodcastName() {
        return mPodcastName.substring(0, mPodcastName.lastIndexOf('.'));
    }

    public void setPodcastName(String p) {
        this.mPodcastName = p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mIndex);
        parcel.writeString(mPodcastName);
        parcel.writeString(mPodcastURI);
    }
}
