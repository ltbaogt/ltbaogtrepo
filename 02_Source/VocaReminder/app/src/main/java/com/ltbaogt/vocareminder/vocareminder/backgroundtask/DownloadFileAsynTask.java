package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fahc03-177 on 10/14/16.
 */
public class DownloadFileAsynTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        Log.d("AAA", ">>>DownloadFileAsynTask START");
        String mp3Url = strings[0];
        String filesDir = strings[1] + "/mp3";
        Log.d("AAA", ">>>DownloadFileAsynTask mp3Dir= " + filesDir);
        File mp3Dir = new File(filesDir);
        if (!mp3Dir.exists()) {
            mp3Dir.mkdirs();
        }
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mp3Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Error";
            }
//            int fileLength = connection.getContentLength();
            String mp3FilePath = filesDir + "/hello.mp3";
            checkFile(mp3FilePath);
            Log.d("AAA", ">>>DownloadFileAsynTask mp3file= " + mp3FilePath);
            is = connection.getInputStream();
            os = new FileOutputStream(mp3FilePath);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = is.read(data)) != 1) {
                total +=count;
                os.write(data,0,count);
            }

        } catch (Exception e) {

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {

            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private void checkFile(String mp3FilePath) {
        File fi = new File(mp3FilePath);
        Log.d("AAA", ">>>DownloadFileAsynTask.checkFile = " + fi.exists());
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(mp3FilePath);
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

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.d("AAA", ">>>DownloadFileAsynTask END");
    }
}
