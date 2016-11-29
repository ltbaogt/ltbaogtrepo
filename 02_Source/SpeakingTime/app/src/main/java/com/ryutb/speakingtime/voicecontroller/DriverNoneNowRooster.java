package com.ryutb.speakingtime.voicecontroller;

import android.content.Context;

import com.ryutb.speakingtime.bean.AlarmObject;

/**
 * Created by MyPC on 29/11/2016.
 */
public class DriverNoneNowRooster extends DriverRooster {

    public static final String HOUR_NON_NOW_PREFIX = "h_nn_";
    public DriverNoneNowRooster(Context ctx, int volume, int alarmType) {
        super(ctx, volume, alarmType);
    }

    @Override
    protected int prepareMediaPlayerHour(int hour) {
        int hourRes = mContext.getResources()
                .getIdentifier(HOUR_NON_NOW_PREFIX + hour, "raw", mContext.getPackageName());
        return hourRes;
    }
}
