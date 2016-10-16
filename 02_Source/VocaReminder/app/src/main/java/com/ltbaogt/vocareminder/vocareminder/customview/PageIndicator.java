package com.ltbaogt.vocareminder.vocareminder.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

/**
 * Created by MyPC on 07/10/2016.
 */
public class PageIndicator extends LinearLayout {

    public static final String TAG = Define.TAG + "PageIndicator";
    private int mPage;
    private int mCurrentPage = -1;
    private LinearLayout mIndicators;

    public PageIndicator(Context context) {
        super(context);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNumberPages(int page) {
        mPage = page;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        VRLog.d(TAG, ">>>init START, mPage=" + mPage);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (inflater != null) {
            LinearLayout v = (LinearLayout) inflater.inflate(R.layout.page_indicator_layout, this);
            mIndicators = (LinearLayout) v.findViewById(R.id.outter_layout);
            LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
            lp.setMargins(5, 0, 0, 0);
            for (int i = 0; i < mPage; i++) {
                View page = new View(getContext());
                page.setBackgroundColor(Color.GREEN);
                mIndicators.addView(page, lp);
            }
        }
        VRLog.d(TAG, ">>>init END");
    }


    public void setCurrentPage(int cPage) {
        if (mIndicators != null && (cPage < 0 || cPage >= mIndicators.getChildCount())) {
            return;
        }
        setCurrentBackground(mIndicators.getChildAt(cPage));
        mCurrentPage = cPage;
        for (int i = 0; i < mIndicators.getChildCount(); i++) {
            if (i != cPage) {
                setNotCurrentBackground(mIndicators.getChildAt(i));
            }
        }
    }

    private void setCurrentBackground(View v) {
        setBackground(v, true);
    }

    private void setNotCurrentBackground(View v) {
        setBackground(v, false);
    }

    private void setBackground(View v, boolean isCurrent) {
        if (isCurrent) {
            v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_selected));
        } else {
            v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_unselected));
        }
    }
}
