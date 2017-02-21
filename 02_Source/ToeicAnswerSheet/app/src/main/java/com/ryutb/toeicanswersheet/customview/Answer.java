package com.ryutb.toeicanswersheet.customview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ryutb.toeicanswersheet.R;
import com.ryutb.toeicanswersheet.utils.AppConstant;

import java.util.HashMap;

/**
 * Created by MyPC on 19/02/2017.
 */
public class Answer extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    private static final int SELECTED_LETTER = 1;
    private static final int DESELECTED_LETTER = 0;
    private static final int SELECTED_KEY_LETTER = 0;
    private int mSenNumber = -1;
    TextView mSentenceNumber;
    TextView mLetterA;
    TextView mLetterB;
    TextView mLetterC;
    TextView mLetterD;
    HashMap<View, Integer> mLetterArray;

    private OnChoiceListener mOnChoiceListner;

    public interface OnChoiceListener {
        void onChoiceLetter(Answer thisView, int number);
    }

    public Answer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public void setOnChoiceListener(OnChoiceListener l) {
        mOnChoiceListner = l;
    }

    private void initLayout() {
        inflate(getContext(), R.layout.answer_layout, this);
        mLetterArray = new HashMap<>();
        mSentenceNumber = (TextView) findViewById(R.id.id_sentence);
        mLetterA = (TextView) findViewById(R.id.id_letter_a);
        mLetterB = (TextView) findViewById(R.id.id_letter_b);
        mLetterC = (TextView) findViewById(R.id.id_letter_c);
        mLetterD = (TextView) findViewById(R.id.id_letter_d);

        mLetterA.setTag(AppConstant.LETTER_A);
        mLetterB.setTag(AppConstant.LETTER_B);
        mLetterC.setTag(AppConstant.LETTER_C);
        mLetterD.setTag(AppConstant.LETTER_D);

        //Set click event
        mLetterA.setOnClickListener(this);
        mLetterB.setOnClickListener(this);
        mLetterC.setOnClickListener(this);
        mLetterD.setOnClickListener(this);

        mLetterA.setOnLongClickListener(this);
        mLetterB.setOnLongClickListener(this);
        mLetterC.setOnLongClickListener(this);
        mLetterD.setOnLongClickListener(this);

        //Put into hashmap to perform loop
        mLetterArray.put(mLetterA, DESELECTED_LETTER);
        mLetterArray.put(mLetterB, DESELECTED_LETTER);
        mLetterArray.put(mLetterC, DESELECTED_LETTER);
        mLetterArray.put(mLetterD, DESELECTED_LETTER);
    }


    public void setSentenceNumber(int number) {
        mSenNumber = number;
        mSentenceNumber.setText(String.valueOf(number + 1));
    }

    public int getSentenceNumber() {
        return mSenNumber;
    }

    public void setChoiceLetter(int number) {
        View vTemp = null;
        switch (number) {
            case AppConstant.LETTER_A:
                vTemp = mLetterA;
                break;
            case AppConstant.LETTER_B:
                vTemp = mLetterB;
                break;
            case AppConstant.LETTER_C:
                vTemp = mLetterC;
                break;
            case AppConstant.LETTER_D:
                vTemp = mLetterD;
                break;
            default:
                resetChoice();
                break;
        }
        if (vTemp != null) {
            onClick(vTemp);
        }
    }

    private void resetChoice() {
        for (View viewElement : mLetterArray.keySet()) {
            mLetterArray.put(viewElement, DESELECTED_LETTER);
            viewElement.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle));
        }
    }
    @Override
    public void onClick(View view) {
        mLetterArray.put(view, SELECTED_LETTER);
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_select));
        for (View viewElement : mLetterArray.keySet()) {
            if (viewElement != view) {
                mLetterArray.put(viewElement, DESELECTED_LETTER);
                viewElement.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle));
            }
        }
        if (mOnChoiceListner != null) {
            mOnChoiceListner.onChoiceLetter(this, (Integer) view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        mLetterArray.put(view, SELECTED_KEY_LETTER);
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_double));
        for (View viewElement : mLetterArray.keySet()) {
            if (viewElement != view
                    && mLetterArray.get(viewElement) != SELECTED_LETTER) {
                mLetterArray.put(viewElement, DESELECTED_LETTER);
                viewElement.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle));
            }
        }
        return true;
    }
}
