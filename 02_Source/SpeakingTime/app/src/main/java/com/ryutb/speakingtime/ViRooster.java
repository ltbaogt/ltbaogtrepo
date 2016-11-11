package com.ryutb.speakingtime;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public class ViRooster extends Rooster {


    public ViRooster(Context ctx) {
        super(ctx);
    }

    //Minus speak completed listener
    MediaPlayer.OnCompletionListener mMinusSpeakCompleted = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mOnSpeakCompleted != null) {
                mOnSpeakCompleted.onSpeakCompleted();
                doRepeat(mediaPlayer, 5000);
            }
        }
    };

    @Override
    public void speakNow() throws IOException {

        Calendar calendar = GregorianCalendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minus = calendar.get(Calendar.MINUTE);

        MediaPlayer mediaPlayer = prepareMediaPlayerHour(hourOfDay);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer2) {

                mediaPlayer2 = prepareMediaPlayerMinute(minus);
                if (mediaPlayer2 != null) {
                    mediaPlayer2.setOnCompletionListener(mMinusSpeakCompleted);
                    mediaPlayer2.start();
                }
            }
        });
    }

    @Override
    protected MediaPlayer prepareMediaPlayerHour(int hour) {
        int hourRes = mContext.getResources()
                .getIdentifier(HOUR_PREFIX + hour, "raw", mContext.getPackageName());
        return prepareMediaPlayer(hourRes);
    }

    @Override
    protected MediaPlayer prepareMediaPlayerMinute(int minute) {
        int hourRes = mContext.getResources()
                .getIdentifier(MINUTE_PREFIX + minute, "raw", mContext.getPackageName());
        return prepareMediaPlayer(hourRes);
    }
}
