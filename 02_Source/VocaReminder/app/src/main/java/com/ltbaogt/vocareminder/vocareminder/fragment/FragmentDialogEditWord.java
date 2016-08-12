package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

public class FragmentDialogEditWord extends DialogFragment implements View.OnClickListener{

    private Word mWord;
    private EditText mEtWordName;
    private EditText mEtMeaning;
    private EditText mEtSentence;
    private Button mBtnCancel;
    private Button mBtnSave;
    private Button mBtnDelete;

    public FragmentDialogEditWord () {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_word, container, false);
        mBtnCancel = (Button) v.findViewById(R.id.btn_cancel);
        mBtnSave = (Button) v.findViewById(R.id.btn_save);
        mBtnDelete = (Button) v.findViewById(R.id.btn_delete);
        mBtnCancel.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        Bundle b = getArguments();
        getDialog().setTitle("Thêm từ mới");
        if (b != null) {
            getDialog().setTitle("Chỉnh sửa từ của bạn");
            mWord = b.getParcelable(Define.WORD_OBJECT_PARCELABLE);
            mEtWordName = (EditText) v.findViewById(R.id.et_name);
            mEtMeaning = (EditText) v.findViewById(R.id.et_meaning);
            mEtWordName.setText(mWord.getWordName());
            mEtMeaning.setText(mWord.getDefault_Meaning());
        }

        return v;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                mWord.setDefault_Meaning(mEtMeaning.getText().toString());
                OALBLL bl = new OALBLL(view.getContext());
                bl.updateWord(mWord);
                dismiss();
                break;
            case R.id.btn_delete:
                int idWord = mWord.getWordId();
                bl = new OALBLL(view.getContext());
                bl.deleteWordById(idWord);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
