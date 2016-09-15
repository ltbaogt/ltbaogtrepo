package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.runnable.ShowHideViewRunnable;

import java.lang.ref.WeakReference;

/**
 * Created by MyPC on 14/09/2016.
 */
public class ShowMeaningCheckChanged implements CompoundButton.OnCheckedChangeListener {

    WeakReference<TextView> mTvSentence;

    public ShowMeaningCheckChanged(TextView tv) {
        mTvSentence = new WeakReference<>(tv);
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        {
            if (mTvSentence != null && mTvSentence.get() != null)
            if (b) {
                mTvSentence.get().setAlpha(0);
                mTvSentence.get().setVisibility(View.VISIBLE);
                mTvSentence.get().animate().alpha(1).setDuration(500).start();
            } else {
                ShowHideViewRunnable r = new ShowHideViewRunnable();
                r.setView(mTvSentence.get());
                mTvSentence.get().animate().alpha(0).setDuration(500).withEndAction(r).start();
            }
        }
    }
}
