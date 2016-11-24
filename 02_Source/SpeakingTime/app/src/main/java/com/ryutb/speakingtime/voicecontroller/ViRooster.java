package com.ryutb.speakingtime.voicecontroller;

import android.content.Context;

import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.voicecontroller.Rooster;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public class ViRooster extends Rooster {

    private static final String TAG = "MyClock";

    public ViRooster(Context ctx, AlarmObject ao) {
        super(ctx, ao);
    }

    public ViRooster(Context ctx, AlarmObject ao, boolean isRepeat) {
        super(ctx, ao);
        setIsRepeat(isRepeat);
    }

    public ViRooster(Context ctx, int volume) {
        super(ctx, volume);
    }

    @Override
    protected int prepareMediaPlayerHour(int hour) {
        int hourRes = mContext.getResources()
                .getIdentifier(HOUR_PREFIX + hour, "raw", mContext.getPackageName());
        return hourRes;
    }

    @Override
    protected int prepareMediaPlayerMinute(int minute) {
        int minuteRes = mContext.getResources()
                .getIdentifier(MINUTE_PREFIX + minute, "raw", mContext.getPackageName());
        return minuteRes;
    }


}
