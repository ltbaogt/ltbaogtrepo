package com.ryutb.speakingtime.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ryutb.speakingtime.activity.AlarmListActivity;
import com.ryutb.speakingtime.activity.MainActivity;
import com.ryutb.speakingtime.R;
import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.sql.VCDatabaseOpenHelper;
import com.ryutb.speakingtime.util.Define;

import java.util.ArrayList;

/**
 * Created by MyPC on 21/11/2016.
 */
public class AlarmListTabFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = Define.createTAG("AlarmListTabFragment");
    private VCDatabaseOpenHelper mDbHelper;
    private EditText mEtAlarmId;
    private TextView mTvAlarmIdList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alarm_list_fragment_layout, container, false);
        Button btnNewAlarm = (Button) v.findViewById(R.id.btn_add_new_alarm);
        btnNewAlarm.setOnClickListener(this);

        Button btnCancelAlarm = (Button) v.findViewById(R.id.btn_cancel_alarm);
        btnCancelAlarm.setOnClickListener(this);
        mEtAlarmId = (EditText) v.findViewById(R.id.et_alarm_id);

        mTvAlarmIdList = (TextView) v.findViewById(R.id.tv_alarm_list_id);
        mDbHelper = new VCDatabaseOpenHelper(getContext());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<AlarmObject> listAlarm = mDbHelper.getAllAlarms();
        String idList = "";
        for (AlarmObject a: listAlarm) {
            idList += a.toString() + ",\n";
            Log.d(TAG, ">>>onCreateView " + a.toString());
        }
        mTvAlarmIdList.setText(idList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_new_alarm:
                newAlarm();
                break;
            case R.id.btn_cancel_alarm:
                cancelAlarm();
                break;
        }
    }

    private void newAlarm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void cancelAlarm() {
        try {
            int alarmId = Integer.valueOf(mEtAlarmId.getText().toString());
            PendingIntent pIntent = ((AlarmListActivity) getActivity())
                    .createPendingIntent(alarmId, PendingIntent.FLAG_NO_CREATE);
            if (pIntent != null) {
                pIntent.cancel();
                if (mDbHelper.cancelAlarmById(alarmId) <= 0) {
                    Toast.makeText(getContext(), "Alarm is cancelled but cannot delete this alarm", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, ">>>cancelAlarm Alarm is cancelled but cannot delete this alarm");
                } else {
                    Toast.makeText(getContext(), "Alarm is deleted successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, ">>>cancelAlarm is cancelled successfully");
                }
            } else {
                Toast.makeText(getContext(), "Alarm not found", Toast.LENGTH_SHORT).show();
                Log.d(TAG, ">>>cancelAlarm Alarm not found");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Input Alarm", Toast.LENGTH_SHORT).show();
        }
    }
}
