package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by My PC on 10/08/2016.
 */

public class FragmentDialogEditWord extends DialogFragment implements View.OnClickListener {

    private Word mWord;
    private EditText mEtWordName;
    private EditText mEtMeaning;
    private EditText mEtSentence;
    private Button mBtnCancel;
    private Button mBtnSave;
    private OnDismissDialogListener mOnDismissDialogListener;

    public interface OnDismissDialogListener {
        void onDismissDialog();
    }

    public void setOnDismissDialogListener(OnDismissDialogListener l) {
        mOnDismissDialogListener = l;
    }

    public FragmentDialogEditWord() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_word, container, false);
        Bundle b = getArguments();
        mBtnCancel = (Button) v.findViewById(R.id.btn_cancel);
        mBtnSave = (Button) v.findViewById(R.id.btn_save);
        mBtnCancel.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);

        String btn1Title = b.getString(Define.POPUP_BUTTON_01, "--");
        String btn2Title = b.getString(Define.POPUP_BUTTON_02, "--");
        mBtnCancel.setText(btn1Title);
        mBtnSave.setText(btn2Title);
        if (b != null) {

            getDialog().setTitle(b.getString(Define.POPUP_TITLE, "Default String"));
            mWord = b.getParcelable(Define.WORD_OBJECT_PARCELABLE);
            mEtWordName = (EditText) v.findViewById(R.id.et_name);
            mEtMeaning = (EditText) v.findViewById(R.id.et_meaning);
            mEtWordName.setText(mWord.getWordName());
            mEtMeaning.setText(mWord.getDefault_Meaning());
        }

        return v;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDismissDialogListener) {
            ((OnDismissDialogListener)activity).onDismissDialog();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                mWord.setWordName(mEtWordName.getText().toString());
                mWord.setDefault_Meaning(mEtMeaning.getText().toString());
                OALBLL bl = new OALBLL(view.getContext());
                if (mWord.getWordId() != -1) {
                    bl.updateWord(mWord);
                } else {
                    bl.addNewWord(mWord);
                }

                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
