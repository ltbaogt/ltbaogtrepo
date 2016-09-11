package com.ltbaogt.vocareminder.vocareminder.runnable;

import android.view.View;

/**
 * Created by MyPC on 11/09/2016.
 */
public class ShowHideViewRunnable implements Runnable {

    private View mView;
    public void setView(View v) {
        mView = v;
    }
    @Override
    public void run() {
        mView.setVisibility(View.INVISIBLE);
    }
}
