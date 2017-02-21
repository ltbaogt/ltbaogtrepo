package com.ryutb.toeicanswersheet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ryutb.toeicanswersheet.R;
import com.ryutb.toeicanswersheet.customview.Answer;

import java.util.ArrayList;

/**
 * Created by MyPC on 19/02/2017.
 */
public class SentenceAdapter extends BaseAdapter implements Answer.OnChoiceListener {
    ArrayList<com.ryutb.toeicanswersheet.model.Answer> mArrayList;

    public SentenceAdapter(ArrayList<com.ryutb.toeicanswersheet.model.Answer> list) {
        mArrayList = list;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            convertView = inflater.inflate(R.layout.sentence_layout, null);
        }
        Answer answer = (Answer) convertView.findViewById(R.id.sentence_view);
        answer.setOnChoiceListener(this);
        answer.setSentenceNumber(mArrayList.get(i).getSentenceNumber());
        answer.setChoiceLetter(mArrayList.get(i).getChoiceLetter());
        return convertView;
    }

    @Override
    public void onChoiceLetter(Answer thisView, int number) {
        mArrayList.get(thisView.getSentenceNumber()).setChoiceLetter(number);
    }
}
