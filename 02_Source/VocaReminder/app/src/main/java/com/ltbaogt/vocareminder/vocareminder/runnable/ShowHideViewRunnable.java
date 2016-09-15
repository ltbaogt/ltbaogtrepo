package com.ltbaogt.vocareminder.vocareminder.runnable;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by MyPC on 11/09/2016.
 */
public class ShowHideViewRunnable implements Runnable {

    private WeakReference<View> mView;
    public void setView(View v) {
        mView = new WeakReference<>(v);
    }
    @Override
    public void run() {
        if (mView != null && mView.get() != null) {
            mView.get().setVisibility(View.INVISIBLE);
        }
    }
}
