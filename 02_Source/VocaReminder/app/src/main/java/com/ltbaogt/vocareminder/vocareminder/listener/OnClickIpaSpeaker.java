package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.bean.Word;

/**
 * Created by MyPC on 08/10/2016.
 */
public class OnClickIpaSpeaker implements View.OnClickListener {

    private Word mWord;
    public OnClickIpaSpeaker(Word w) {
        mWord = w;
    }
    @Override
    public void onClick(View view) {
        mWord.playMp3IfNeed(view.getContext());
    }



}
