package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.DownloadFileAsynTask;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import java.io.IOException;

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
        String mp3FileName = VRStringUtil.mp3ForWordName(mWord.getWordName());
        VRStringUtil.playMp3IfNeed(view.getContext(), mp3FileName, mWord.getMp3Url());
    }



}
