package com.ltbaogt.vocareminder.vocareminder.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.adapter.MeaningAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 06/10/2016.
 */
public class SlidePageFragment extends Fragment implements MeaningAdapter.OnCheckMeaningListener {

    private static final String TAG = Define.TAG + "SlidePageFragment";
    private WordEntity mEntity;
    RecyclerView mRecyclerView;

    public SlidePageFragment() {}

    public void setEntity(WordEntity entity) {
        mEntity = entity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slide_page_layout, container, false);
        ((TextView) v.findViewById(R.id.tv_position)).setText(mEntity.position);
        createListMeaning(v);
        return v;
    }

    private void createListMeaning(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.sg_list_meaning);
        MeaningAdapter ma = new MeaningAdapter(mEntity.meanings);
        ma.setOnCheckMeaningListener(this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(ma);
    }


    public WordEntity getEntity() {
        if (mEntity != null) {
            mEntity.selectedMeaning = getSelectedMeaning();
        }
        return mEntity;
    }
    public int getSelectedMeaning() {
        int index = -1;
        if (mRecyclerView != null) {
            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            if (adapter != null && (adapter instanceof MeaningAdapter)) {
                index = ((MeaningAdapter) adapter).getSelectedIndex();
            }
        }
        return index;
    }

    @Override
    public void onCheckMeaning(int selectedPosition) {
        mEntity.selectedMeaning = selectedPosition;
    }
}
