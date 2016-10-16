package com.ltbaogt.vocareminder.vocareminder.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.Mp3FinishPlayingListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by MyPC on 06/10/2016.
 */
public class Utils {

    public static final String TAG = Define.TAG + "Utils";

    public static String UpperFirstCharacterOrString(String s) {
        String retString = "";
        if (null != s) {
            retString = s.substring(0,1).toUpperCase() + s.substring(1);
        }
        return  retString;
    }


    public static void playMp3InLocal(Context ctx, String filename) {
        Log.d(TAG, ">>>playMp3InLocal START");
        String mp3FilePath = Utils.getMp3FileDir(ctx) + filename;
        playMp3File(mp3FilePath);
        Log.d(TAG, ">>>playMp3InLocal END");
    }

    public static void playMp3File(final String dataSource) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, ">>>playMp3File START");
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(dataSource);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(new Mp3FinishPlayingListener(mediaPlayer));
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String mp3ForWordName(String wordName) {
        if (wordName != null) {
            wordName = wordName.toLowerCase();
        }
        return wordName + ".mp3";
    }
    public static String formatMeaning(String s) {
        return "- " + Utils.UpperFirstCharacterOrString(s);
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

    public static String getFilesDir(Context ctx) {
        ContextWrapper cw = new ContextWrapper(ctx);
        return cw.getFilesDir().getPath();
    }

    public static String getMp3FileDir(Context ctx) {
        String mp3Path = getFilesDir(ctx) + "/mp3/";
        File fi = new File(mp3Path);
        if (!fi.exists()) {
            fi.mkdirs();
        }
        return mp3Path;
    }

    public static boolean checkMp3FileExisted(Context ctx, String mp3File) {
        File file = new File(getMp3FileDir(ctx) + mp3File);
        return file.exists();

    }

    public static File mp3FileForName(Context ctx, String mp3File) {
        return new File(getMp3FileDir(ctx) + mp3File);
    }
}
