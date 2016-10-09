package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;

/**
 * Created by MyPC on 09/10/2016.
 */
public class FragmentSuggestion extends DialogFragment {

    private LinearLayout mSuggestions;
    private LayoutInflater mInflater;
    private SparseArray<String> mArraySuggestion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        View v = mInflater.inflate(R.layout.fragment_popup_suggestions, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mSuggestions = (LinearLayout) v.findViewById(R.id.suggestions);
        createSuggestionItems(mArraySuggestion.size());
        return v;
    }

    public void setArray(SparseArray<String> a) {
        mArraySuggestion = a;
    }

    private void createSuggestionItems(int num) {
        if (mSuggestions == null) return;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < num; i++) {
            LinearLayout item = (LinearLayout) mInflater.inflate(R.layout.suggestion_item, mSuggestions, false);
            ((TextView) item.findViewById(R.id.tv_stt)).setText("" + mArraySuggestion.keyAt(i));
            ((TextView) item.findViewById(R.id.tv_suggestion)).setText("" + mArraySuggestion.valueAt(i));
            mSuggestions.addView(item, lp);
        }
    }
}
