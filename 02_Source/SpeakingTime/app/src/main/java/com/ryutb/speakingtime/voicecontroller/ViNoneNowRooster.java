package com.ryutb.speakingtime.voicecontroller;

import android.content.Context;

import com.ryutb.speakingtime.bean.AlarmObject;

public class ViNoneNowRooster extends ViRooster {

    public static final String HOUR_NON_NOW_PREFIX = "h_nn_";
    public ViNoneNowRooster(Context ctx, AlarmObject ao, int alarmType) {
        super(ctx, ao, alarmType);
    }

    @Override
    protected int prepareMediaPlayerHour(int hour) {
        int hourRes = mContext.getResources()
                .getIdentifier(HOUR_NON_NOW_PREFIX + hour, "raw", mContext.getPackageName());
        return hourRes;
    }
}
