package com.ryutb.toeicanswersheet.model;

/**
 * Created by MyPC on 19/02/2017.
 */
public class Answer {
    public int getSentenceNumber() {
        return mSentenceNumber;
    }

    public void setSentenceNumber(int p) {
        this.mSentenceNumber = p;
    }

    public int getChoiceLetter() {
        return mChoiceLetter;
    }

    public void setChoiceLetter(int p) {
        this.mChoiceLetter = p;
    }

    int mSentenceNumber;
    int mChoiceLetter;
}
