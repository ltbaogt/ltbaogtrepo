package com.ltbaogt.vocareminder.vocareminder.bean;

import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import java.util.ArrayList;

/**
 * Created by MyPC on 06/10/2016.
 */
public class WordEntity extends BaseWord {

    public String wordName;
    public String pronunciation;
    public String mp3URL;
    public String position;
    public ArrayList<String> meanings = new ArrayList<>();
    public int selectedMeaning = -1;

    public String toString() {
        return "{"
                + "name = " + wordName
                + " pronun = " + pronunciation
                + " mp3Url = " + mp3URL
                + " posistion = " + position
                + " meaning = {" + printMeaning() + "}"
                + "}";
    }


    public void addMeaning(String s) {
        int lastIndexColon = s.lastIndexOf(':');
        if (lastIndexColon == s.length() - 1) {
            s = s.substring(0, s.length() - 1);
        }
        meanings.add(VRStringUtil.UpperFirstCharacterOrString(s.trim()));
    }

    public String printMeaning() {
        String ret = "";
        for (String s : meanings) {
            ret += '\n' + s;
        }
        return ret;
    }

    public String getSelectedMeaning() {
        if (selectedMeaning < 0 || selectedMeaning >= meanings.size()) {
            return "";
        }
        return VRStringUtil.formatMeaning(meanings.get(selectedMeaning));
    }

    @Override
    public String getWordName() {
        return wordName;
    }

    @Override
    public String getMp3Url() {
        return mp3URL;
    }
}
