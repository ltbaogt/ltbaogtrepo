package com.ryutb.speakingtime.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.ryutb.speakingtime.R;
import com.ryutb.speakingtime.activity.MainActivity;
import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.service.VoiceClockService;

/**
 * Created by MyPC on 21/11/2016.
 */
public class AlarmForDriverFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alarm_for_driver_fragment_layout, container, false);

        final AlarmObject alarmObject = new AlarmObject(getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE));
        Button btnNewAlarm = (Button) v.findViewById(R.id.btn_add_new_alarm);
        btnNewAlarm.setOnClickListener(this);

        Button btnStop = (Button) v.findViewById(R.id.btn_stop_service);
        btnStop.setOnClickListener(this);

        AppCompatCheckBox cbPlayOnHeadPhone = (AppCompatCheckBox) v.findViewById(R.id.cbPlayOnHeadPhone);
        cbPlayOnHeadPhone.setChecked(alarmObject.getIsPlayOnHeadPhone());
        cbPlayOnHeadPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                alarmObject.setIsPlayOnHeadPhone(b);
            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_stop_service) {
            Intent intent = new Intent(getActivity().getApplicationContext(), VoiceClockService.class);
            getActivity().stopService(intent);
        } else {
            startAlarmForDriver();
        }
    }

    public void startAlarmForDriver() {
        Intent intent = new Intent(getActivity().getApplicationContext(), VoiceClockService.class);
        getActivity().startService(intent);
    }
}
