package com.ltbaogt.vocareminder.vocareminder.listener;

import android.media.MediaPlayer;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 16/10/2016.
 */
public class Mp3FinishPlayingListener implements MediaPlayer.OnCompletionListener {
    public static final String TAG = Define.TAG + "Mp3FinishPlayingListener";
    MediaPlayer mMp3Player;
    public Mp3FinishPlayingListener(MediaPlayer mp3) {
        mMp3Player = mp3;
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.release();
        Log.d(TAG, ">>>playMp3File END");
    }
}
