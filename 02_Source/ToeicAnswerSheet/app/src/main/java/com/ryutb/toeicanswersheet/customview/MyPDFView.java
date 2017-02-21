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
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mTimePressed <= 0 || ev.getAction() == MotionEvent.ACTION_MOVE && resetTimeWhenMove(ev)) {
            curX = getX();
            curY = getY();
            mTimePressed = System.currentTimeMillis();
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - mTimePressed > 2000) {
                mTimePressed = 0;
                if (mLongPressViewListener != null) {
                    mLongPressViewListener.onLongPressPDF();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean resetTimeWhenMove(MotionEvent ev) {
        boolean isResetTime = false;
        if (ev.getRawX() - curX > 150 || ev.getRawY() - curY > 150) {
            isResetTime = true;
        }
        return isResetTime;
    }
}
