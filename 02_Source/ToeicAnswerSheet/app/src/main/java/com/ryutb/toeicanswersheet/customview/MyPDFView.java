package com.ryutb.toeicanswersheet.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.github.barteksc.pdfviewer.PDFView;

/**
 * Created by MyPC on 21/02/2017.
 */
public class MyPDFView extends PDFView {

    private static final String TAG = "MyPDFView";
    private long mTimePressed = 0;
    private float curX;
    private float curY;
    private static final int ALOW_MOVE_DISTANCE = 50;

    private OnLongPressPDFView mLongPressViewListener;

    public void setOnLongPressPDFView(OnLongPressPDFView l) {
        mLongPressViewListener = l;
    }

    public interface OnLongPressPDFView {
        void onLongPressPDF();
    }

    /**
     * Construct the initial view
     *
     * @param context
     * @param set
     */
    public MyPDFView(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, ">>>dispatchTouchEvent action=" + ev.getAction());
        if ((ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_MOVE) && mTimePressed <= 0 || resetTimeWhenMove(ev)) {
            Log.d(TAG, ">>>dispatchTouchEvent reset time");
            curX = ev.getX();
            curY = ev.getY();
            mTimePressed = System.currentTimeMillis();
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d(TAG, ">>>dispatchTouchEvent check time");
            if (System.currentTimeMillis() - mTimePressed > 1000) {
                Log.d(TAG, ">>>dispatchTouchEvent fire long touch");
                mTimePressed = 0;
                if (mLongPressViewListener != null) {
                    mLongPressViewListener.onLongPressPDF();
                }
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mTimePressed = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean resetTimeWhenMove(MotionEvent ev) {
        boolean isResetTime = false;
        Log.d(TAG, String.format(">>>resetTimeWhenMove distanceX=(%1$s,%2$s) distanceX=(%3$s,%4$s)", ev.getX(), curX, ev.getY(), curY));
        if (Math.abs(ev.getX() - curX) > ALOW_MOVE_DISTANCE || Math.abs(ev.getY() - curY) > ALOW_MOVE_DISTANCE) {
            isResetTime = true;
        }
        Log.d(TAG, ">>>resetTimeWhenMove isResetTime=" + isResetTime);
        return isResetTime;
    }
}
