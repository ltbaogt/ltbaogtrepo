package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

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

    private Context mContext;

    public DownloadFileAsynTask(Context ctx) {
        mContext = ctx;
    }
    @Override
    protected String doInBackground(String... strings) {
        Log.d("AAA", ">>>DownloadFileAsynTask START");
        String mp3Url = strings[0];
        String mp3FileName = strings[1];
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        String savedFile = null;

        try {
            URL url = new URL(mp3Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                savedFile = "Error";
                return savedFile;
            }
            File fi = new File(VRStringUtil.getMp3FileDir(mContext) + "/" + mp3FileName);
            is = connection.getInputStream();
            os = new FileOutputStream(fi);
            byte data[] = new byte[4096];
            int count;
            while ((count = is.read(data)) != -1) {
                os.write(data,0,count);
            }
            savedFile = fi.getPath();
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
        return savedFile;
    }

    @Override
    protected void onPostExecute(String mp3File) {
        super.onPostExecute(mp3File);
        Log.d("AAA", ">>>DownloadFileAsynTask file save at= " + mp3File);
        VRStringUtil.playMp3File(mp3File);
    }
}
