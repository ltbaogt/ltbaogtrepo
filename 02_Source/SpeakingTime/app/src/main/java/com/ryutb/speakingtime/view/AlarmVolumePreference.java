package com.ryutb.speakingtime.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ryutb.speakingtime.R;

/**
 * Created by MyPC on 20/11/2016.
 */
public class AlarmVolumePreference extends Preference {

    private SeekBar mVolumeSeekBar;

    public AlarmVolumePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mVolumeSeekBar = (SeekBar) holder.findViewById(R.id.st_volume_alarm);

        AudioManager am = (AudioManager) mVolumeSeekBar.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mVolumeSeekBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        mVolumeSeekBar.setProgress(am.getStreamMaxVolume(AudioManager.STREAM_ALARM)/2);
    }

    public int getAlarmVolume() {
        if (mVolumeSeekBar != null) {
            return mVolumeSeekBar.getProgress();
        }
        return 0;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return super.onGetDefaultValue(a, index);
    }
}
