package com.ryutb.myexample.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ryutb.myexample.R;
import com.ryutb.myexample.modal.Podcast;
import com.ryutb.myexample.util.PodcastConstant;

import java.io.IOException;

public class PodcastPlayerActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;
    ImageView mPlayControl;
    MediaState mMediaState;
    ImageView mPodcastImage;
    MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();


    public enum MediaState {
        PAUSE,
        PLAYING,
        STOP,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);
        mPlayControl = (ImageView) findViewById(R.id.play_control);
        mPodcastImage = (ImageView) findViewById(R.id.podcast_image);
        Bundle startedIntent = getIntent().getExtras();
        Podcast selectedPodcast = startedIntent.getParcelable(PodcastConstant.EXTRA_PODCAST_OBJECT);

        myRetriever.setDataSource(selectedPodcast.getPodcastPath());
        setArtwork(myRetriever);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(selectedPodcast.getPodcastPath());
            mMediaState = MediaState.STOP;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playClicked(View v) throws IOException {
        if (mMediaPlayer != null) {
            if (!mMediaPlayer.isPlaying()) {
                if (mMediaState == MediaState.STOP) {
                    mMediaPlayer.prepare();
                }
                mMediaPlayer.start();
                mPlayControl.setImageResource(R.drawable.pause);
                mMediaState = MediaState.PLAYING;
            } else {
                mMediaPlayer.pause();
                mPlayControl.setImageResource(R.drawable.play);
                mMediaState = MediaState.PAUSE;
            }
        }
    }

    public void stopClicked(View v) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.stop();
            mMediaState = MediaState.STOP;
            mPlayControl.setImageResource(R.drawable.play);
        }
    }

    public void seekClicked(View v) {
        if (mMediaPlayer != null) {
            if (mMediaState == MediaState.PLAYING) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                mMediaPlayer.seekTo(currentPosition + PodcastConstant.SEEK_TIME);
            }
        }
    }

    public boolean setArtwork(MediaMetadataRetriever myRetriever) {
        byte[] artwork;

        artwork = myRetriever.getEmbeddedPicture();

        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            mPodcastImage.setImageBitmap(bMap);

            return true;
        } else {
            mPodcastImage.setImageBitmap(null);

            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    private void stopPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }
}
