package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.runnable.ShowHideViewRunnable;

/**
 * Created by MyPC on 14/09/2016.
 */
public class ShowMeaningCheckChanged implements CompoundButton.OnCheckedChangeListener {

    TextView mTvSentence;

    public ShowMeaningCheckChanged(TextView tv) {
        mTvSentence = tv;
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        {
            if (b) {
                mTvSentence.setAlpha(0);
                mTvSentence.setVisibility(View.VISIBLE);
                mTvSentence.animate().alpha(1).setDuration(500).start();
            } else {
                ShowHideViewRunnable r = new ShowHideViewRunnable();
                r.setView(mTvSentence);
                mTvSentence.animate().alpha(0).setDuration(500).withEndAction(r).start();

            }
        }
    }
}
