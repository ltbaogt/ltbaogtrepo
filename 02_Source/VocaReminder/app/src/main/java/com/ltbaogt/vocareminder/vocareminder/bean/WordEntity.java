package com.ltbaogt.vocareminder.vocareminder.bean;

import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;

import java.util.ArrayList;

/**
 * Created by MyPC on 06/10/2016.
 */
public class WordEntity {

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
                +"}";
    }

    public String printMeaning() {
        String ret = "";
        for (String s : meanings) {
            ret+= '\n' + s ;
        }
        return ret;
    }

    public String getSelectedMeaning() {
        return meanings.get(selectedMeaning);
    }
}
