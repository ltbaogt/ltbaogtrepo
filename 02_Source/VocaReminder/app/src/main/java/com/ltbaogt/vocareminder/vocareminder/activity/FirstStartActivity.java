package com.ltbaogt.vocareminder.vocareminder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.customview.PageIndicator;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentIntructionPage;
import com.ltbaogt.vocareminder.vocareminder.listener.VROnPageChangeListener;

/**
 * Created by MyPC on 16/10/2016.
 */
public class FirstStartActivity extends AppCompatActivity {

    private static final int PAGE_NUMER = 5;
    private ViewPager mPager;
    private PagerAdapter mAdaper;
    private PageIndicator mPageIndicator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_start_main_layout);
        SharedPreferences sp = getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(Define.REF_FIRST_START, false).apply();
        mPager = (ViewPager) findViewById(R.id.image);
        mAdaper = new PageSlider(getSupportFragmentManager());
        mPager.setAdapter(mAdaper);
        mPageIndicator = (PageIndicator) findViewById(R.id.indicator);
        mPageIndicator.setNumberPages(PAGE_NUMER);
        VROnPageChangeListener onPageChange = new VROnPageChangeListener(mPageIndicator);
        mPager.removeOnPageChangeListener(onPageChange);
        mPager.addOnPageChangeListener(onPageChange);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mPageIndicator.setCurrentPage(mPager.getCurrentItem());
    }

    public void finishTutorial(View v) {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(mainActivity);
        finish();
    }
    public class PageSlider extends FragmentStatePagerAdapter {

        public PageSlider(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new FragmentIntructionPage();
        }

        @Override
        public int getCount() {
            return PAGE_NUMER;
        }
    }
}
