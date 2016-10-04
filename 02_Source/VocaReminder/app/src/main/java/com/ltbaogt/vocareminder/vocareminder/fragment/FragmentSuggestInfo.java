package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.adapter.MeaningAdapter;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MyPC on 03/10/2016.
 */
public class FragmentSuggestInfo extends DialogFragment {
    private ArrayList<HashMapItem> mArrayMeaning;
    private String mPronun;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_info_layout, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView tvPronun = (TextView) v.findViewById(R.id.sg_pronun);
        tvPronun.setText(mPronun);
        createListMeaning(v);
        return v;
    }

    private void createListMeaning(View v) {
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.sg_list_meaning);
        MeaningAdapter ma = new MeaningAdapter(mArrayMeaning);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(ma);
    }

    public void setArrayList(ArrayList<HashMapItem> array) {
        mArrayMeaning = array;
    }

    public void setPronun(String pronun) {
        mPronun = pronun;
    }
}
