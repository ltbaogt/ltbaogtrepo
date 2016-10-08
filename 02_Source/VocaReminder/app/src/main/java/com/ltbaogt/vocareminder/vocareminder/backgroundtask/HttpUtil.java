package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.os.AsyncTask;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
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

    public static final String ENTRY_BODY = ".entry-body";
    public static final String TAG_POS = ".posgram.ico-bg";
    public static final String TAG_WORD_NAME = ".headword";
    public static final String TAG_DEFINE = ".def";

    public interface OnFinishLoadWordDefine {
        void onFinishLoad(Document doc);
    }

    public static ArrayList<WordEntity> getWordInfo(Document doc) {
        ArrayList<WordEntity> listWord = new ArrayList<>();
        if (doc != null) {
            Elements entryBody = doc.select(ENTRY_BODY);
            if (entryBody != null && entryBody.size() > 0) {
                Element entry = entryBody.get(0);
                String ipa = getPronunciation(doc);
                String mp3Url = getMp3Url(doc);
                String wordName = "---";
                Elements wordNameEls = entry.select(TAG_WORD_NAME);
                if (wordNameEls != null && wordNameEls.size() > 0) {
                    wordName = wordNameEls.first().text();
                }
                //Create single word
                for (int i = 0; i < entry.children().size(); i++) {
                    WordEntity wordEntry = new WordEntity();
                    wordEntry.wordName = VRStringUtil.UpperFirstCharacterOrString(wordName);
                    wordEntry.pronunciation = ipa;
                    wordEntry.mp3URL = mp3Url;
                    Element entryBodyEl = entry.child(i);

                    Log.d(TAG, ">>>getWordInfo classname of Child= " + entryBodyEl.className());
                    Elements posEls = entryBodyEl.select(TAG_POS);
                    Log.d(TAG, ">>>getWordInfo posEls.size= " + posEls.size());
                    if (posEls != null && posEls.size() > 0) {
                        Element postEl = posEls.first();
                        wordEntry.position = VRStringUtil
                                .UpperFirstCharacterOrString(postEl.text());
                    } else {
                        //Unable to fetch position of Word
                    }

                    Elements defEls = entryBodyEl.select(TAG_DEFINE);
                    if (defEls != null && defEls.size() > 0) {
                        for (Element e : defEls) {
                            wordEntry.addMeaning(e.text());
                        }
                    } else {
                        //Unable to fetch definition of Word
                    }
                    Log.d(TAG, ">>>getWordInfo " + wordEntry.toString());
                    listWord.add(wordEntry);
                }
            } else {
                //No thing to select
            }
        } else {
            Log.d(TAG, ">>>getWordInfo Unable to fetch document");
        }
        return listWord;
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
