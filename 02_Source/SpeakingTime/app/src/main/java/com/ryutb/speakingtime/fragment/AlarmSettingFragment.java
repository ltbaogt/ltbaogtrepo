package com.ryutb.speakingtime.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.ryutb.speakingtime.R;
import com.ryutb.speakingtime.view.AlarmVolumePreference;

/**
 * Created by MyPC on 11/11/2016.
 */
public class AlarmSettingFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.alarm_setting_fragment,rootKey);
    }

    public int getVolume() {
        AlarmVolumePreference volume = (AlarmVolumePreference)findPreference("st_volume");
        return volume.getAlarmVolume();
    }

}
