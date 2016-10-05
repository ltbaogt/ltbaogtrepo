package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.os.AsyncTask;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ppp on 31/08/2016.
 */
public class HttpUtil {
    private static final String TAG = Define.TAG + "HttpUtil";
    public static final String url = "http://dictionary.cambridge.org/dictionary/english/";
    public static final String mp3Tag = "span.circle.circle-btn.sound.audio_play_button.uk";
    public static final String mp3TagDataSrcMp3 = "data-src-mp3";

    public static final String pronunTag = "span.pron";
    public static final String meaningTag = ".def";

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
    public static ArrayList<HashMapItem> getMeanings(Document doc) {
        try {
            ArrayList<HashMapItem> array = new ArrayList<>();
            Elements elements = doc.select(meaningTag);
            Log.d(TAG, ">>>getMeaning size=" + elements.size());
            for(Element e : elements) {
                String meaning = formatMeaning(e.text());
                HashMapItem item = new HashMapItem();
                item.put(Define.EXTRA_MEANING, meaning);
                item.setIndex(0);
                array.add(item);
                Log.d(TAG, ">>>getMeaning meaning=" + meaning);
            }
            return array;
        }catch (NullPointerException e) {
            return null;
        }
    }

    public static String formatMeaning(String s) {
        String retString = s.substring(0,1).toUpperCase() + s.substring(1);
        return "- " + retString.substring(0, s.length() - 1);
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
