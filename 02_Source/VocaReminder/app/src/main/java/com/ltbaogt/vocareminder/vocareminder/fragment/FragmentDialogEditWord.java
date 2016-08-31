package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.HttpUtil;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by My PC on 10/08/2016.
 */

public class FragmentDialogEditWord extends DialogFragment implements View.OnClickListener {

    private static final String TAG = Define.TAG + "FragmentDialogEditWord";
    private Word mWord;
    private EditText mEtWordName;
    private EditText mEtMeaning;
    private EditText mEtSentence;
    private Button mBtnCancel;
    private Button mBtnSave;
    private ImageView mBtnGetInfo;
    private OnCreateOrUpdateWodListener mOnCreateOrUpdateWodListener;
    private ProgressBar mLoading;

    public interface OnCreateOrUpdateWodListener {
        void onSave(Word w);
        void onUpdate(Word w);
    }

    public void setOnCreateOrUpdateWodListener(OnCreateOrUpdateWodListener l) {
        mOnCreateOrUpdateWodListener = l;
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
        mBtnGetInfo = (ImageView) v.findViewById(R.id.btn_get_info);
        mLoading = (ProgressBar) v.findViewById(R.id.ctrlActivityIndicator);
        mBtnCancel.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnGetInfo.setOnClickListener(this);

        String btn1Title = b.getString(Define.POPUP_BUTTON_01, "--");
        String btn2Title = b.getString(Define.POPUP_BUTTON_02, "--");
        mBtnCancel.setText(btn1Title);
        mBtnSave.setText(btn2Title);
        if (b != null) {
            mEtWordName = (EditText) v.findViewById(R.id.et_name);
            mEtMeaning = (EditText) v.findViewById(R.id.et_meaning);

            getDialog().setTitle(b.getString(Define.POPUP_TITLE, "Default Popup"));
            mWord = b.getParcelable(Define.WORD_OBJECT_PARCELABLE);
            int popupType = b.getInt(Define.POPUP_TYPE, Define.POPUP_NEW_WORD);
            if (popupType == Define.POPUP_NEW_WORD) {
                mEtWordName.setHint(mWord.getWordName());
                mEtMeaning.setHint(mWord.getDefault_Meaning());
            } else {
                mEtWordName.setText(mWord.getWordName());
                mEtMeaning.setText(mWord.getDefault_Meaning());
            }
            mEtWordName.requestFocus();
            showKeyboard();
        }
        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        hideKeyboard();
    }

    private void showKeyboard() {
        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyboard() {
        Log.d(TAG, ">>>hideKeyboard START");
        try {
            Activity activity = getActivity();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } catch (NullPointerException e) {
            Log.d(TAG, "Unable to hide keyboard");
        }
        Log.d(TAG, ">>>hideKeyboard END");
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                String wordName = mEtWordName.getText().toString();
                //Word name is empty
                if ("".equalsIgnoreCase(wordName.trim())) {
                    Toast toast = Toast.makeText(getActivity(), R.string.word_is_empty, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                    getDialog().getWindow().getDecorView().animate().setDuration(50).translationX(50).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            FragmentDialogEditWord.this.getDialog().getWindow().getDecorView().animate().setDuration(50).translationX(0).start();
                        }
                    }).start();
//                    getDialog().getWindow().getDecorView().animate().translationX(-100).setDuration(2000).start();
                    return;
                }
                mWord.setWordName(wordName);
                mWord.setDefault_Meaning(mEtMeaning.getText().toString());
                OALBLL bl = new OALBLL(view.getContext());
                if (mWord.getWordId() != -1) {
                    bl.updateWord(mWord);
                    if (mOnCreateOrUpdateWodListener != null) {
                        mOnCreateOrUpdateWodListener.onUpdate(mWord);
                    }
                } else {
                    bl.addNewWord(mWord);
                    if (mOnCreateOrUpdateWodListener != null) {
                        mOnCreateOrUpdateWodListener.onSave(mWord);
                    }
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_get_info:
                mLoading.setVisibility(View.VISIBLE);
                mBtnGetInfo.setVisibility(View.INVISIBLE);
                getInfo();
                break;
        }
    }

    private void getInfo() {
        HttpUtil.OnFinishLoadWordDefine onloadFinish = new HttpUtil.OnFinishLoadWordDefine() {
            @Override
            public void onFinishLoad(Document doc) {
                mLoading.setVisibility(View.INVISIBLE);
                mBtnGetInfo.setVisibility(View.VISIBLE);

                String mp3Url = HttpUtil.getMp3Url(doc);
                Log.d(TAG, ">>>onFinishLoad= " + mp3Url);
                if (mp3Url != null) {
                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(mp3Url);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        Log.d(TAG, ">>>onFinishLoad play mp3 error" + Log.getStackTraceString(e));
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.word_not_found, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                }

                String pronun = HttpUtil.getPronunciation(doc);
                if (pronun != null) {
                    mEtMeaning.setText(pronun);
                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.word_not_found, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                }

            }
        };
        HttpUtil.LoadWordDefine task = new HttpUtil.LoadWordDefine(mEtWordName.getText().toString(), onloadFinish);
        task.execute();

    }
}
