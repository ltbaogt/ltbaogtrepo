package com.ryutb.speakingtime;

import android.os.Bundle;
import android.preference.PreferenceActivity;

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
