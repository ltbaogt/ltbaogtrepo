package com.ltbaogt.vocareminder.vocareminder.backgroundtask;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 15/10/2016.
 */
public class DictionaryFactory {

    public FetchContentDictionarySite getDictionaryTypeInstance(String type) {
        FetchContentDictionarySite dict = null;
        if (Define.REF_DICTIONARY_TYPE_EN.equals(type)) {
            dict = new CambrigeDictionarySite();
        } else if (Define.REF_DICTIONARY_TYPE_VI.equals(type)) {
            dict = new EnViDictionarySite();
        }
        return dict;
    }
}
