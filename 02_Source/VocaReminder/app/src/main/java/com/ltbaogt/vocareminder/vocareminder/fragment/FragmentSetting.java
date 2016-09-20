package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.content.Context;
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
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.OnCheckNotificationQuickAdd;
import com.ltbaogt.vocareminder.vocareminder.listener.ServiceRunningListener;

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

    TextView mBackupDesription;
    TextView mRestoreDescription;

    private Switch mSwitchQuickAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        findView(v);

        if(getMainActivity() != null) {
            int dismissTime = getMainActivity().getProviderWrapper().getDismissTime() / 1000;
            mSeekbarDismissTime.setOnSeekBarChangeListener(this);
            mSeekbarDismissTime.setProgress(dismissTime);
            mTvDismissTime.setText(dismissTime + "s");
            int serviceStatus = getMainActivity().getProviderWrapper().getServiceRunningStatus();
            mSwitchServiceRunning.setChecked((serviceStatus == 1 ? true:false));
            mBackupDesription = (TextView) v.findViewById(R.id.backup_description);
            mRestoreDescription = (TextView) v.findViewById(R.id.restore_description);
            String backupFilePath = getActivity().getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE).getString(Define.BACKUP_PATH, "Not set");
            if (backupFilePath != null) {
                backupFilePath = backupFilePath.split("\\.")[0];
            }
            mBackupDesription.setText(backupFilePath);
            mRestoreDescription.setText(backupFilePath);

            mSwitchQuickAdd = (Switch) v.findViewById(R.id.setting_quick_add_switch);
            mSwitchQuickAdd.setOnCheckedChangeListener(new OnCheckNotificationQuickAdd());
        }
        return v;
    }

    private MainActivity getMainActivity() {
        if (getActivity() instanceof MainActivity) {
            return (MainActivity)getActivity();
        }
        return null;
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
        if (getMainActivity() != null) {
            getMainActivity().getProviderWrapper().updateDismissTime(mCurrentDismissTime * 1000);
        }
    }


}
