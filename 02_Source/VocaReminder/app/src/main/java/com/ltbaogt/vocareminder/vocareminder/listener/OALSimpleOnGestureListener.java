package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by MyPC on 06/09/2016.
 */
public class OALSimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

    public interface OnDoubleTap {
        void onDoubleTap();
    }

    private OnDoubleTap mOnDoubleTapListener;

    public void setOnDoubleTapListener(OnDoubleTap listener) {
        mOnDoubleTapListener = listener;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mOnDoubleTapListener != null) {
            mOnDoubleTapListener.onDoubleTap();
        }
        return super.onDoubleTap(e);
    }
}
