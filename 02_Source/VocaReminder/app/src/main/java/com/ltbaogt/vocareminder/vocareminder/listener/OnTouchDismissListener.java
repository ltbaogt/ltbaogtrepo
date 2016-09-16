package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by MyPC on 14/09/2016.
 */
public class OnTouchDismissListener implements View.OnTouchListener {

    private GestureDetector mGesture;
    public OnTouchDismissListener(GestureDetector g) {
        mGesture = g;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGesture.onTouchEvent(motionEvent);
    }
}
