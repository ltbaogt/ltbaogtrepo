package com.ltbaogt.vocareminder.vocareminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;

/**
 * Created by MyPC on 11/10/2016.
 */
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    private SparseArray<String> mArraySuggestion;
    public OnSuggestionItemClicked onSuggestionItemClicked;

    public SuggestionAdapter(SparseArray<String> array) {
        mArraySuggestion = array;
    }
public interface OnSuggestionItemClicked  {
    void onSuggestionItemClicked(String wordName);
}
    private View.OnClickListener mOnClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof String) {
                String suggestion = (String) view.getTag();
                if (onSuggestionItemClicked != null) {
                    onSuggestionItemClicked.onSuggestionItemClicked(suggestion);
                }
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.suggestion_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stt.setText("" + (mArraySuggestion.keyAt(position) + 1));
        holder.suggestion.setText("" + mArraySuggestion.valueAt(position));
        holder.item.setTag(mArraySuggestion.valueAt(position));
        holder.item.setOnClickListener(mOnClickItem);
    }

    @Override
    public int getItemCount() {
        return mArraySuggestion.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item;
        TextView stt;
        TextView suggestion;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            stt = ((TextView) itemView.findViewById(R.id.tv_stt));
            suggestion = ((TextView) itemView.findViewById(R.id.tv_suggestion));
        }
    }

}
