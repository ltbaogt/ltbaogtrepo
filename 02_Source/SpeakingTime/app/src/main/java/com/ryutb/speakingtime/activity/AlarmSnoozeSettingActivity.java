package com.ryutb.speakingtime.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ryutb.speakingtime.fragment.AlarmSnoozeSettingFragment;

/**
 * Created by MyPC on 11/11/2016.
 */
public class AlarmSnoozeSettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new AlarmSnoozeSettingFragment()).commit();
    }
}
