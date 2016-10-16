package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.listener.OnCheckNotificationQuickAdd;
import com.ltbaogt.vocareminder.vocareminder.listener.ServiceRunningListener;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

import java.io.File;

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
            mSwitchServiceRunning.setChecked((((serviceStatus == OALService.SERVICE_RUNNING_YES) && isServiceRunning()) ? true:false));
            mBackupDesription = (TextView) v.findViewById(R.id.backup_description);
            mRestoreDescription = (TextView) v.findViewById(R.id.restore_description);
            mBackupDesription.setText(getBackupFile());
            mRestoreDescription.setText(getBackupFile());

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
    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (OALService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setStartStopServiceToggle(boolean isStart) {
        VRLog.d("AAA", ">>>setStartStopServiceToggle START");
        mSwitchServiceRunning.setChecked(isStart);
        VRLog.d("AAA", ">>>setStartStopServiceToggle END");
    }

    private String getBackupFile() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String backupFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + MainActivity.BACKUP_FOLDER + MainActivity.BACKUP_FILE;
            File backupFile = new File(backupFilePath);
            if (backupFile.exists()) {
                return backupFile.getName();
            }
            return "Not set";
        } else {
            return "SDCard doesn't exist";
        }
    }

    public void setBackupFile(String backup) {
        mBackupDesription.setText(backup);
        mRestoreDescription.setText(backup);
    }
}
