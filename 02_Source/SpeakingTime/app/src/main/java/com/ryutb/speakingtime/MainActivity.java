package com.ryutb.speakingtime;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void speakHour(View v) throws IOException {
        Calendar calendar = GregorianCalendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minus = calendar.get(Calendar.MINUTE);
        Uri mp3 = Uri.parse("android.resource://"
                + getPackageName() + "/raw/"
                + "h_" + hourOfDay);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), mp3);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer2) {

                Uri mp32 = Uri.parse("android.resource://"
                        + getPackageName() + "/raw/"
                        + "minute_" + minus);
                mediaPlayer2 = MediaPlayer.create(getApplicationContext(), mp32);
                if (mediaPlayer2 != null) {
                    mediaPlayer2.start();
                }
            }
        });
    }
}
