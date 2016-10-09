package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.util.SparseArray;

import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by MyPC on 09/10/2016.
 */
public interface FetchContentDictionarySite {
    ArrayList<WordEntity> getWordInfo(Document doc);
    SparseArray<String> getSuggestions(Document doc);
    String getUrl();
    String getSuggestionUrl();
}
