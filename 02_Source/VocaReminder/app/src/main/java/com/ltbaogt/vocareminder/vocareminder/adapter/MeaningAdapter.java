package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MyPC on 03/10/2016.
 */
public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> mMeaningArray;
    private int currentSelectionMeaning = 0;

    public MeaningAdapter(ArrayList<HashMap<String, String>> arr) {
        mMeaningArray = arr;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.suggest_meaning_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mCbChoose.setText(mMeaningArray.get(position).get(Define.EXTRA_MEANING));
        holder.mCbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    currentSelectionMeaning++;
                    holder.mCbChoose.setText("" + currentSelectionMeaning);
                } else {
                    currentSelectionMeaning--;
                    holder.mCbChoose.setText("");
                }

            }
        });
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
