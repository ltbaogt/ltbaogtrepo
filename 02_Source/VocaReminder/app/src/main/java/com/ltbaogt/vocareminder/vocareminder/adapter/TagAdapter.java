package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Tag;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.util.ArrayList;

/**
 * Created by MyPC on 06/09/2016.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private static final String TAG = Define.TAG + "TagAdapter";
    private ArrayList<Tag> mArrayList;

    public TagAdapter(ArrayList<Tag> tagArray) {
        mArrayList = tagArray;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        final Tag tag = mArrayList.get(position);
        holder.mTvTagName.setText(tag.getTagName());
        holder.mTagItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = tag.getTagID();
                Log.d(TAG, ">>>onBindViewHolder>>>onClick tagId= " + id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTagName;
        public View mTagItem;
        public TagViewHolder(View itemView) {
            super(itemView);
            mTagItem = itemView.findViewById(R.id.tag_item);
            mTvTagName = (TextView) itemView.findViewById(R.id.tag_name);

        }
    }
}
