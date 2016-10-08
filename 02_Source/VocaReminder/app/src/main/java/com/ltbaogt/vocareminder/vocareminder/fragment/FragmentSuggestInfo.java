package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.customview.PageIndicator;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.VROnPageChangeListener;
import com.ltbaogt.vocareminder.vocareminder.pager.SlidePageFragment;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import java.util.ArrayList;

/**
 * Created by MyPC on 03/10/2016.
 */
public class FragmentSuggestInfo extends DialogFragment {
    private static final String TAG = Define.TAG + "FragmentSuggestInfo";
    private ArrayList<WordEntity> mArrayMeaning;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private PageIndicator mPageIndicator;
    private AcceptSuggestionListener mAcceptSuggestionListener;
    public interface AcceptSuggestionListener {
        void onDismiss(WordEntity entity);
    }

    public void setAcceptSuggestionListener(AcceptSuggestionListener l) {
        mAcceptSuggestionListener = l;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_info_layout, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView tvWrdName = (TextView) v.findViewById(R.id.sg_word_name);
        TextView tvPronun = (TextView) v.findViewById(R.id.sg_pronun);
        ImageView imgIpa = (ImageView) v.findViewById(R.id.sg_ipa);
        ImageView imgAccept = (ImageView) v.findViewById(R.id.accept_this_suggestion);
        imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentEntityIndex = mPageIndicator.getCurrentPage();
                Log.d(TAG, ">>>onClick currentEntityIndex= " + currentEntityIndex);
                WordEntity selectedEntity = mArrayMeaning.get(currentEntityIndex);
                if (mAcceptSuggestionListener != null) {
                    mAcceptSuggestionListener.onDismiss(selectedEntity);
                }
                getDialog().dismiss();
            }
        });
        mPageIndicator = (PageIndicator) v.findViewById(R.id.page_indicator);

        if (mArrayMeaning != null && mArrayMeaning.size() > 0) {
            mPageIndicator.setNumberPages(mArrayMeaning.size());
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
        createPager(v);
        return v;
    }

    private void createPager(View v) {
        mPager = (ViewPager) v.findViewById(R.id.meaning_pager);
        mPagerAdapter = new SlidePagePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        VROnPageChangeListener pageChangeListener = new VROnPageChangeListener(mPageIndicator);
        mPager.removeOnPageChangeListener(pageChangeListener);
        mPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, ">>> onAttachFragment");
        if (mPager != null) {
            mPageIndicator.setCurrentPage(mPager.getCurrentItem());
        }
    }

    public void setArrayList(ArrayList<WordEntity> array) {
        mArrayMeaning = array;
    }

    private class SlidePagePagerAdapter extends FragmentStatePagerAdapter {

        public SlidePagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, ">>>getItem position= " + position);
            SlidePageFragment pageFragment = new SlidePageFragment();
            pageFragment.setEntity(mArrayMeaning.get(position));
            return pageFragment;
        }

        @Override
        public int getCount() {
            return mArrayMeaning.size();
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Log.d(TAG, ">>>instantiateItem position= " + position);
//            super.instantiateItem(container, position);
////            SlidePageFragment f = (SlidePageFragment)
//            SlidePageFragment pageFragment = new SlidePageFragment();
//            pageFragment.setEntity(mArrayMeaning.get(position));
//            registeredFragments.put(position, pageFragment);
//            return pageFragment;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            Log.d(TAG, ">>>destroyItem position= " + position);
//            registeredFragments.remove(position);
//            super.destroyItem(container, position, object);
//        }
    }

}
