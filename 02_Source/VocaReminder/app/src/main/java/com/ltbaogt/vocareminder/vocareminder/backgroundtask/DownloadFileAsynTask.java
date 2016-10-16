package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.content.Context;
import android.os.AsyncTask;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.Utils;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fahc03-177 on 10/14/16.
 */
public class DownloadFileAsynTask extends AsyncTask<String, Void, String> {

    public static final String TAG = Define.TAG + "DownloadFileAsynTask";
    private Context mContext;

    public DownloadFileAsynTask(Context ctx) {
        mContext = ctx;
    }
    @Override
    protected String doInBackground(String... strings) {
        VRLog.d(TAG, ">>>DownloadFileAsynTask START");
        String savedFile = null;
        //TODO: Index out of range
        if (strings.length >= 2) {
            String mp3Url = strings[0];
            String mp3FileName = strings[1];
            if (mp3FileName.lastIndexOf(".mp3") <= 0) {
                VRLog.d(TAG, "Mp3 file name is invalid");
            }
            InputStream is = null;
            OutputStream os = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(mp3Url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    savedFile = "Error";
                    return savedFile;
                }
                File fi = new File(Utils.getMp3FileDir(mContext) + "/" + mp3FileName);
                is = connection.getInputStream();
                os = new FileOutputStream(fi);
                byte data[] = new byte[4096];
                int count;
                while ((count = is.read(data)) != -1) {
                    os.write(data, 0, count);
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
        }
        return savedFile;
    }

    @Override
    protected void onPostExecute(String mp3File) {
        super.onPostExecute(mp3File);
        VRLog.d(TAG, ">>>DownloadFileAsynTask file save at= " + mp3File);
        if (!Utils.isStringNullOrEmpty(mp3File)) {
            Utils.playMp3File(mp3File);
        } else {
            VRLog.d(TAG, ">>>DownloadFileAsynTask mp3 file path is null");
            Utils.showToastAtTop(mContext, R.string.this_word_have_not_sound);
        }
    }
}
