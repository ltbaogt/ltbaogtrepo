package com.ltbaogt.vocareminder.vocareminder.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
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
        if (isStringNullOrEmpty(url)) {
            Log.d(TAG, ">>>playMp3File URL not found");
        }
        new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MediaPlayer mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setDataSource(url);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                Log.d(TAG, ">>>playMp3File START");
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                        Log.d(TAG, ">>>playMp3File STOP");
                                    }
                                });
                            } catch (Exception e) {
                                Log.d(TAG, ">>>playMp3File play mp3 error" + Log.getStackTraceString(e));
                            }
                        }
                    }).start();
    }

    public static String formatMeaning(String s) {
        return "- " + VRStringUtil.UpperFirstCharacterOrString(s);
    }

    public static boolean isStringNullOrEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void showToastAtTop(Context ctx, final int strId) {
        Toast toast = Toast.makeText(ctx, strId, Toast.LENGTH_SHORT);
        if (toast != null) {
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }
    public static void showToastAtBottom(Context ctx, final int strId) {
        Toast toast = Toast.makeText(ctx, strId, Toast.LENGTH_SHORT);
        if (toast != null) {
            toast.show();
        }
    }
}
