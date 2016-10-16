package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by MyPC on 16/10/2016.
 */
public class Mp3UrlForName extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String mWordname;

    public Mp3UrlForName(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        mWordname = strings[0];
        String mp3Url = null;
        if (!VRStringUtil.isStringNullOrEmpty(mWordname)) {
            try {
                CambrigeDictionarySite dictionarySite = new CambrigeDictionarySite();
                String url = dictionarySite.getUrl() + mWordname.toLowerCase();
                Document doc = Jsoup.connect(url).get();
                mp3Url = dictionarySite.getMp3Url(doc);
            } catch (Exception e) {

            }
        }
        return mp3Url;
    }

    @Override
    protected void onPostExecute(String url) {
        DownloadFileAsynTask downloader = new DownloadFileAsynTask(mContext);
        downloader.execute(url, VRStringUtil.mp3ForWordName(mWordname));
    }
}
