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
    private static final String TAG = Define.TAG + "HttpUtil";
    public interface OnFinishLoadWordDefine {
        void onFinishLoad(Document doc);
    }

    public static class LoadWordDefine extends AsyncTask<String, Void, Document> {
        private static final String TAG = Define.TAG + "LoadWordDefine";
        private String mForWord;
        private OnFinishLoadWordDefine mOnFinishLoadWordDefine;
        private String mUrl;

        public LoadWordDefine(String atSite, String forWord, OnFinishLoadWordDefine l) {
            forWord = forWord.trim().toLowerCase();
            mForWord = forWord;
            mOnFinishLoadWordDefine = l;
            mUrl = atSite;
        }

        @Override
        protected Document doInBackground(String... params) {
            Log.d(TAG, ">>>doInBackground START");
            if (mForWord == null || mForWord.length() == 0) {
                Log.d(TAG, ">>>doInBackground word empty");
            }
            Document doc = null;
            try {
                String url = mUrl + mForWord;
                Log.d(TAG, ">>>doInBackground url=" + url);
                doc = Jsoup.connect(url).get();
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
