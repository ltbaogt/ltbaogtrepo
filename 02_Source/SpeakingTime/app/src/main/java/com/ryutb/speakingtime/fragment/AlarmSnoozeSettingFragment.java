package com.ryutb.speakingtime.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ryutb.speakingtime.R;

/**
 * Created by MyPC on 11/11/2016.
 */
public class AlarmSnoozeSettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.snooze_setting_fragment);
    }
}
