package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.listener.ServiceRunningListener;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;
import com.ltbaogt.vocareminder.vocareminder.shareref.OALShareReferenceHepler;

/**
 * Created by My PC on 09/08/2016.
 */

public class FragmentSetting extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    //Time to dismiss
    TextView mTvDismissTime;
    SeekBar mSeekbarDismissTime;
    private int mCurrentDismissTime;

    //Service running
    Switch mSwitchServiceRunning;
    ServiceRunningListener mServiceRunningListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        findView(v);
//        mSwitchServiceRunning.setChecked(OALService.isStarted());

        OALShareReferenceHepler sr = new OALShareReferenceHepler(getContext());
        int dismissTime = sr.getDismissTime() / 1000;
        mSeekbarDismissTime.setOnSeekBarChangeListener(this);
        mSeekbarDismissTime.setProgress(dismissTime);
        mTvDismissTime.setText(dismissTime + "s");
        return v;
    }

    private void findView(View v) {
        mTvDismissTime = (TextView) v.findViewById(R.id.setting_dismiss_time_value);
        mSeekbarDismissTime = (SeekBar) v.findViewById(R.id.setting_dismiss_time_sb);
        mSwitchServiceRunning = (Switch) v.findViewById(R.id.setting_service_switch_switch);
        mServiceRunningListener = new ServiceRunningListener(getActivity());
        mSwitchServiceRunning.setOnCheckedChangeListener(mServiceRunningListener);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
        mCurrentDismissTime = value;
        mTvDismissTime.setText(mCurrentDismissTime + "s");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTvDismissTime.setText(mCurrentDismissTime + "s");
        OALShareReferenceHepler sr = new OALShareReferenceHepler(getContext());
        sr.setDismissTime(mCurrentDismissTime * 1000);
    }

}
