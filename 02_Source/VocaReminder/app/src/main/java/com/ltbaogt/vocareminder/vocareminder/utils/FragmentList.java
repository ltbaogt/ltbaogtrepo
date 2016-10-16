package com.ltbaogt.vocareminder.vocareminder.utils;

import com.ltbaogt.vocareminder.vocareminder.fragment.BaseFragment;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentAbout;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentListWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MyPC on 30/09/2016.
 */
public class FragmentList {
    public final static String MAIN_FRAGMENT_TAG = "fragment_main";
    public final static String EDIT_FRAGMENT_TAG = "fragment_edit_word";
    public final static String ABOUT_FRAGMENT_TAG = "fragment_about";
    public final static String ARCHIVED_FRAGMENT_TAG = "fragment_archived";
    Map<String,BaseFragment> mList;

    public FragmentList() {
        mList = new HashMap<>();
    }

    public void setFragmentForKey(BaseFragment f, String k) {
        if (!mList.containsValue(f)) {
            mList.put(k, f);
        } else {
            VRLog.d("VReminder", ">>>setFragmentForKey fragment already exist");
        }
    }
    public FragmentListWord getVocabularyFragment() {
        return (FragmentListWord) mList.get(MAIN_FRAGMENT_TAG);
    }

    public FragmentListWord getArchievedFragment() {
        return (FragmentListWord) mList.get(ARCHIVED_FRAGMENT_TAG);
    }

    public FragmentSetting getSettingFragment() {
        return (FragmentSetting) mList.get(EDIT_FRAGMENT_TAG);
    }

    public FragmentAbout getAboutFragment() {
        return (FragmentAbout) mList.get(ABOUT_FRAGMENT_TAG);
    }
}
