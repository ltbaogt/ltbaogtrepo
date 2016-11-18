package com.ryutb.speakingtime.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by MyPC on 18/11/2016.
 */
public class AlarmView extends RelativeLayout {

    private static final String TAG = "";

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, ">>>dispatchKeyEvent");
        return super.dispatchKeyEvent(event);
    }
}
