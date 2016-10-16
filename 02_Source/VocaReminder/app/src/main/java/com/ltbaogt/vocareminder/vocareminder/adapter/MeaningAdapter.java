package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.util.ArrayList;

/**
 * Created by MyPC on 03/10/2016.
 */
public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MyViewHolder> {
    private static final String TAG = Define.TAG + "MeaningAdapter";
    private ArrayList<String> mMeaningArray;
    private int selectedPosition = -1;
    private OnCheckMeaningListener mCheckListener;
    //private int maxCurrent = 0;
    //private HashMapItem pHead;

    public MeaningAdapter(ArrayList<String> arr) {
        mMeaningArray = arr;
        //pHead = mMeaningArray.get(mMeaningArray.size() - 1);
    }

    public interface OnCheckMeaningListener {
        void onCheckMeaning(int selectedPosition);
    }

    public void setOnCheckMeaningListener(OnCheckMeaningListener l) {
        mCheckListener = l;
    }
    public int getSelectedIndex() {
        return selectedPosition;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.suggest_meaning_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d("AAA", "bindedPosition= " + position);
        final String itemMeaning = mMeaningArray.get(position);
        holder.mCbChoose.setOnCheckedChangeListener(null);
        holder.mCbChoose.setChecked(selectedPosition == position);
        holder.mCbChoose.setText("" + itemMeaning);
        holder.mCbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                if (mCheckListener != null) {
                    mCheckListener.onCheckMeaning(selectedPosition);
                }
            }
        });
//        holder.mCbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    maxCurrent++;
//                    hasmapItem.inscreaseIndexTo(maxCurrent);
//                } else {
//                    pHead.descreaseIndexFrom(hasmapItem.getIndex());
//                    maxCurrent--;
//                    hasmapItem.inscreaseIndexTo(0);
//                }
//                notifyDataSetChanged();
//            }
//        });
    }

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
