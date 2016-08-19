package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentDialogEditWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentEditWord;

import java.util.ArrayList;

/**
 * Created by My PC on 09/08/2016.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyViewHolder> {

    private static final String TAG = Define.TAG + "DictionaryAdapter";
    private ArrayList<Word> mArrayList;
    private Context mContext;

    public DictionaryAdapter(Context ctx, ArrayList<Word> list) {
        mArrayList = list;
        mContext = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, ">>>onCreateViewHolder START");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_word, parent, false);
        Log.d(TAG, ">>>onCreateViewHolder END");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d(TAG, ">>>onBindViewHolder START");
        Word w = mArrayList.get(position);
        Log.d(TAG, ">>>onBindViewHolder " + w.toString());
        holder.mWordName.setText(w.getWordName());
        holder.mSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity) {
                    Word w = mArrayList.get(holder.getAdapterPosition());
                    ((MainActivity) mContext).showDialogEditWord(w);
                }
            }
        });
        Log.d(TAG, ">>>onBindViewHolder END");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, ">>>getItemCount mArrayList.size= " + mArrayList.size());
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView mWordName;
        protected CardView mSingleItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            mWordName = (TextView) itemView.findViewById(R.id.tv_title_wordname);
            mSingleItem = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
