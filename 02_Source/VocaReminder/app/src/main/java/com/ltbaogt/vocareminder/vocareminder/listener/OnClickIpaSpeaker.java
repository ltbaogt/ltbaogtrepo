package com.ltbaogt.vocareminder.vocareminder.listener;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.DownloadFileAsynTask;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import java.io.IOException;

/**
 * Created by MyPC on 08/10/2016.
 */
public class OnClickIpaSpeaker implements View.OnClickListener {

    private String mUrl;
    public OnClickIpaSpeaker(String url) {
        mUrl = url;
    }
    @Override
    public void onClick(View view) {
//        if (VRStringUtil.isOnline(view.getContext())) {
//            VRStringUtil.playMp3File(mUrl);
//        } else {
//            VRStringUtil.showToastAtBottom(view.getContext(), R.string.you_are_offline);
//        }

//        String mp3Url = "http://dictionary.cambridge.org/media/english/uk_pron/u/ukc/ukcoo/ukcooke021.mp3";
//        String filesDir = VRStringUtil.getFilesDir(view.getContext());
//        DownloadFileAsynTask task = new DownloadFileAsynTask();
//        task.execute(new String[]{mp3Url, filesDir});
        playMp3();
    }

    private void playMp3() {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource("/data/user/0/com.ltbaogt.vocareminder.vocareminder/files/mp3/hello.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    Log.d("AAA", ">>>DownloadFileAsynTask play mp3 stop");
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
