package com.ryutb.speakingtime;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.ryutb.speakingtime.bean.AlarmObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by fahc03-177 on 11/11/16.
 */
public class ViRooster extends Rooster {

    private static final String TAG = "MyClock";

    public ViRooster(Context ctx, AlarmObject ao) {
        super(ctx, ao);
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
