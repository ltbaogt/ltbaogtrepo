package com.ltbaogt.vocareminder.vocareminder.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 06/10/2016.
 */
public class VRStringUtil {

    public static final String TAG = Define.TAG + "VRStringUtil";
    public static String UpperFirstCharacterOrString(String s) {
        String retString = "";
        if (null != s) {
            retString = s.substring(0,1).toUpperCase() + s.substring(1);
        }
        return  retString;
    }

    public static void playMp3File(final String url) {
        new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MediaPlayer mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setDataSource(url);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                    }
                                });
                            } catch (Exception e) {
                                Log.d(TAG, ">>>playMp3File play mp3 error" + Log.getStackTraceString(e));
                            }
                        }
                    }).start();
    }
}
