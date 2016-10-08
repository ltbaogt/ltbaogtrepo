package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

/**
 * Created by MyPC on 08/10/2016.
 */
public class OnClickIpaSpeaker implements View.OnClickListener {

    private String mUrl;
    public OnClickIpaSpeaker(String url) {
        mUrl = url;
    }
    @Override
    public void onClick(View view) {
        VRStringUtil.playMp3File(mUrl);
    }
}
