package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.adapter.MeaningAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.pager.SlidePageFragment;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MyPC on 03/10/2016.
 */
public class FragmentSuggestInfo extends DialogFragment {
    private ArrayList<WordEntity> mArrayMeaning;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_info_layout, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView tvWrdName = (TextView) v.findViewById(R.id.sg_word_name);
        TextView tvPronun = (TextView) v.findViewById(R.id.sg_pronun);
        ImageView imgIpa = (ImageView) v.findViewById(R.id.sg_ipa);
        if (mArrayMeaning != null && mArrayMeaning.size() > 0) {
            final WordEntity entity = mArrayMeaning.get(0);
            tvWrdName.setText(entity.wordName);
            tvPronun.setText(entity.pronunciation);
            imgIpa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VRStringUtil.playMp3File(entity.mp3URL);
                }
            });

        }
//
        createPager(v);
        return v;
    }

    private void createPager(View v) {
        mPager = (ViewPager) v.findViewById(R.id.meaning_pager);
        mPagerAdapter = new SlidePagePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }



    public void setArrayList( ArrayList<WordEntity> array) {
        mArrayMeaning = array;
    }

    private class SlidePagePagerAdapter extends FragmentStatePagerAdapter {

        public SlidePagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("AAA", ">>>getItem");
            SlidePageFragment pageFragment = new SlidePageFragment();
            pageFragment.setEntity(mArrayMeaning.get(position));
            return pageFragment;
        }

        @Override
        public int getCount() {
            return mArrayMeaning.size();
        }
    }
}
