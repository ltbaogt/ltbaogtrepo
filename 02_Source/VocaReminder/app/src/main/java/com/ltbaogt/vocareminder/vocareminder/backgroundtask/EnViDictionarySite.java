package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import android.util.SparseArray;

import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.Utils;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by MyPC on 15/10/2016.
 */
public class EnViDictionarySite extends CambrigeDictionarySite {
    private static final String TAG = Define.TAG + "CambrigeDictionarySite";
    @Override
    public String getWordNameTag() {
        //Indicate the tag has class attribute class="di-title cdo-section-title-hw"
        return ".di-title.cdo-section-title-hw";
    }

    @Override
    public String getDefineTag() {
        return ".pos-body > .def-block.pad-indent > .def-body > .trans";
    }

    @Override
    public String getUrl() {
        return "http://dictionary.cambridge.org/dictionary/english-vietnamese/";
    }

    @Override
    public String getRedirectSuggestionUrl() {
        return "http://dictionary.cambridge.org/search/english-vietnamese/direct/?q=";
    }

    @Override
    public String getSuggestionUrl() {
        return "http://dictionary.cambridge.org/spellcheck/english-vietnamese/?q=";
    }

    @Override
    public String getEntryBodyTag() {
        return ".di.english-vietnamese.entry-body__el.entry-body__el--smalltop.clrd.js-share-holder";
    }

    @Override
    public String getSuggestionTag() {
        return ".unstyled.prefix-block.a--b.a--rev";
    }

    @Override
    public SparseArray<String> getSuggestions(Document doc) {
        SparseArray<String> array = new SparseArray<>();

        if (doc != null) {
            //<li> tags
            Elements suggestionEls = doc.select(getSuggestionTag());
            Elements liTags = null;
            if (suggestionEls != null) {
                if (suggestionEls.size() >= 2) {
                    liTags = suggestionEls.get(1).select("li");
                } else if (suggestionEls.size() > 0) {
                    liTags = suggestionEls.get(0).select("li");
                }
                for (int i = 0; i < liTags.size(); i++) {
                    array.put(i, getPrefix(liTags.get(i)));
                    array.put(i, getPrefixItem(liTags.get(i)));
                }
            }

        }
        return array;
    }

    @Override
    public ArrayList<WordEntity> getWordInfo(Document doc) {
        ArrayList<WordEntity> listWord = new ArrayList<>();
        if (doc != null) {
            Elements entryBody = doc.select(getEntryBodyTag());
            if (entryBody != null && entryBody.size() > 0) {
                Element entry = entryBody.get(0);
                String ipa = getPronunciation(doc);
                String wordName = "---";
                Elements wordNameEls = entry.select(getWordNameTag());
                //Word Name
                if (wordNameEls != null && wordNameEls.size() > 0) {
                    wordName = wordNameEls.first().text();
                }
                //Create single word
                for (int i = 0; i < entryBody.size(); i++) {
                    WordEntity wordEntry = new WordEntity();
                    wordEntry.wordName = Utils.UpperFirstCharacterOrString(wordName);
                    wordEntry.pronunciation = ipa;

                    Elements posEls = entryBody.select(getPositionTag());
                    if (posEls != null && posEls.size() > 0) {
                        VRLog.d(TAG, ">>>getWordInfo posEls.size= " + posEls.size());
                        Element postEl = posEls.first();
                        wordEntry.position = Utils
                                .UpperFirstCharacterOrString(postEl.text());
                    } else {
                        //Unable to fetch position of Word
                    }

                    Elements defEls = entryBody.select(getDefineTag());
                    if (defEls != null && defEls.size() > 0) {
                        for (Element e : defEls) {
                            wordEntry.addMeaning(e.text());
                        }
                    } else {
                        //Unable to fetch definition of Word
                    }
                    VRLog.d(TAG, ">>>getWordInfo " + wordEntry.toString());
                    listWord.add(wordEntry);
                }
            } else {
                //No thing to select
            }
        } else {
            VRLog.d(TAG, ">>>getWordInfo Unable to fetch document");
        }
        return listWord;
    }

}
