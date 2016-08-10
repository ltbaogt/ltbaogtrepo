package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by My PC on 09/08/2016.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyViewHolder> {

    private static final String TAG = Define.TAG + "DictionaryAdapter";
    ArrayList<Word> mArrayList;

    public DictionaryAdapter(ArrayList<Word> list) {
        mArrayList = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_word, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mWordName.setText(mArrayList.get(position).getWordName());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, ">>>getItemCount mArrayList.size= " + mArrayList.size());
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView mWordName;
        public MyViewHolder(View itemView) {
            super(itemView);
            mWordName = (TextView) itemView.findViewById(R.id.tv_title_wordname);
        }
    }
}
