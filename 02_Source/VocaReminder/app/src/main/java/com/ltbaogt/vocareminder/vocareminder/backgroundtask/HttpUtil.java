package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.os.AsyncTask;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by ppp on 31/08/2016.
 */
public class HttpUtil {
    public static final String url = "http://dictionary.cambridge.org/dictionary/english/";
    public static final String mp3Tag = "span.circle.circle-btn.sound.audio_play_button.uk";
    public static final String mp3TagDataSrcMp3 = "data-src-mp3";

    public static final String pronunTag = "span.pron";

    public interface OnFinishLoadWordDefine {
        void onFinishLoad(Document doc);
    }

    public static String getMp3Url(Document doc) {
        try {
            return doc.select(mp3Tag).first().attr(mp3TagDataSrcMp3);
        }catch (NullPointerException e) {
            return null;
        }

    }

    public static String getPronunciation(Document doc) {
        try {
            return doc.select(pronunTag).first().text();
        }catch (NullPointerException e) {
            return null;
        }
    }

    public static class LoadWordDefine extends AsyncTask<String, Void, Document> {
        private static final String TAG = Define.TAG + "LoadWordDefine";
        private String mForWord;
        OnFinishLoadWordDefine mOnFinishLoadWordDefine;

        public LoadWordDefine(String forWord, OnFinishLoadWordDefine l) {
            forWord = forWord.trim().toLowerCase();
            mForWord = forWord;
            mOnFinishLoadWordDefine = l;
        }

        @Override
        protected Document doInBackground(String... params) {
            Log.d(TAG, ">>>doInBackground START");
            if (mForWord == null || mForWord.length() == 0) {
                Log.d(TAG, ">>>doInBackground word empty");
            }
            Document doc = null;
            try {
                doc = Jsoup.connect(url + mForWord).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            mOnFinishLoadWordDefine.onFinishLoad(document);
            Log.d(TAG, ">>>onPostExecute END");
        }
    }
}
