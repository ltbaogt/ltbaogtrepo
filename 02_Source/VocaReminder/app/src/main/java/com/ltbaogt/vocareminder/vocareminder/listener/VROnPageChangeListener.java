package com.ltbaogt.vocareminder.vocareminder.listener;

import android.support.v4.view.ViewPager;

import com.ltbaogt.vocareminder.vocareminder.customview.PageIndicator;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

/**
 * Created by MyPC on 07/10/2016.
 */
public class VROnPageChangeListener implements ViewPager.OnPageChangeListener {

    private static final String TAG = Define.TAG + "VROnPageChangeListener";
    PageIndicator mPageIndicator;

    public VROnPageChangeListener(PageIndicator pI) {
        mPageIndicator = pI;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        VRLog.d(TAG, ">>>onPageSelected START, position= " + position);
        mPageIndicator.setCurrentPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
