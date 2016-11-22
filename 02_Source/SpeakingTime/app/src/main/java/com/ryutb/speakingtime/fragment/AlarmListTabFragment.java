package com.ryutb.speakingtime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ryutb.speakingtime.activity.MainActivity;
import com.ryutb.speakingtime.R;

/**
 * Created by MyPC on 21/11/2016.
 */
public class AlarmListTabFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alarm_list_fragment_layout, container, false);
        Button btnNewAlarm = (Button) v.findViewById(R.id.btn_add_new_alarm);
        btnNewAlarm.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        newAlarm();
    }

    public void newAlarm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
