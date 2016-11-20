package com.ryutb.speakingtime.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

/**
 * Created by MyPC on 20/11/2016.
 */

//This implement helps disable parent's scroll when it scroll
public class SpeakingClockTimePicker extends TimePicker {
    public SpeakingClockTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null) {
                p.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }
}
