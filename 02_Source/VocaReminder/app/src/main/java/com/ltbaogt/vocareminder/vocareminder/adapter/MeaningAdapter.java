package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MyPC on 03/10/2016.
 */
public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MyViewHolder> {
private static final String TAG = Define.TAG + "MeaningAdapter";
    private ArrayList<HashMapItem> mMeaningArray;
    private int maxCurrent = 0;
    private HashMapItem pHead;

    public MeaningAdapter(ArrayList<HashMapItem> arr) {
        mMeaningArray = arr;
        pHead = mMeaningArray.get(mMeaningArray.size() - 1);
//        pHead = mMeaningArray.get(0);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.suggest_meaning_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final HashMapItem hasmapItem = mMeaningArray.get(position);
        holder.mCbChoose.setText("" + hasmapItem.getIndex());
        holder.mCbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    maxCurrent++;
                    hasmapItem.inscreaseIndexTo(maxCurrent);
                } else {
//                    updateIndex(hasmapItem, 0);
                    pHead.descreaseIndexFrom(hasmapItem.getIndex());
                    maxCurrent--;
                    hasmapItem.inscreaseIndexTo(0);
                }
                notifyDataSetChanged();
            }
        });
    }

//    private void updateIndex(HashMapItem item, int withValue) {
//        int oldValue = Integer.valueOf(item.get(Define.EXTRA_INDEX));
//        item.put(Define.EXTRA_INDEX, String.valueOf(withValue));
//        for (int i = 0 ;i < mMeaningArray.size(); i++) {
//            HashMapItem cItem = mMeaningArray.get(i);
//            int currentValue = Integer.valueOf(cItem.get(Define.EXTRA_INDEX));
//            if (item != cItem && currentValue > oldValue) {
//                cItem.put(Define.EXTRA_INDEX, String.valueOf(--currentValue));
//            }
//        }
//    }

    @Override
    public int getItemCount() {
        return mMeaningArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected CheckBox mCbChoose;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCbChoose = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
