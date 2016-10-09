package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.HttpUtil;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.VROnDismisSuggestInfoListener;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by My PC on 10/08/2016.
 */

public class FragmentDialogEditWord extends DialogFragment implements View.OnClickListener {

    private static final String TAG = Define.TAG + "FragmentDialogEditWord";
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    private Word mWord;
    private ImageView mBtnCancel;
    private ImageView mBtnSave;
    private ImageView mBtnGetInfo;
    private OnCreateOrUpdateWodListener mOnCreateOrUpdateWodListener;
    private ProgressBar mLoading;
    private ImageView mBtnVoice;

    private ViewHolder mViewHolder;

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
        mBtnCancel = (ImageView) v.findViewById(R.id.btn_cancel);
        mBtnSave = (ImageView) v.findViewById(R.id.btn_save);
        mBtnGetInfo = (ImageView) v.findViewById(R.id.btn_get_info);
        mLoading = (ProgressBar) v.findViewById(R.id.ctrlActivityIndicator);
        mBtnCancel.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnGetInfo.setOnClickListener(this);
        mViewHolder = new ViewHolder();
        if (checkVoiceRecognition()) {
            mBtnVoice = (ImageView) v.findViewById(R.id.btn_voice);
            mBtnVoice.setVisibility(View.VISIBLE);
            mBtnVoice.setOnClickListener(this);
        }

        if (b != null) {
            mViewHolder.etWordName = (EditText) v.findViewById(R.id.et_name);
            mViewHolder.etMeaning = (EditText) v.findViewById(R.id.et_meaning);
            mViewHolder.etPronunciation = (EditText) v.findViewById(R.id.et_pronunciation);
            mViewHolder.etPosition = (EditText) v.findViewById(R.id.et_position);
            //Set title for dialog
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            //Get word instance
            mWord = b.getParcelable(Define.WORD_OBJECT_PARCELABLE);
            //Get popup type add new of update
            int popupType = b.getInt(Define.POPUP_TYPE, Define.POPUP_NEW_WORD);
            if (popupType == Define.POPUP_NEW_WORD) {
                mViewHolder.etWordName.setHint(mWord.getWordName());
                mViewHolder.etPronunciation.setHint(mWord.getPronunciation());
                mViewHolder.etMeaning.setHint(mWord.getDefault_Meaning());
                mViewHolder.etPosition.setHint(Define.WORD_INIT_POSITION);
            } else {
                mViewHolder.etWordName.setText(mWord.getWordName());
                if (!VRStringUtil.isStringNullOrEmpty(mWord.getPronunciation())) {
                    mViewHolder.etPronunciation.setText(mWord.getPronunciation());
                }
                if (!VRStringUtil.isStringNullOrEmpty(mWord.getDefault_Meaning())) {
                    mViewHolder.etMeaning.setText(mWord.getDefault_Meaning());

                }
                if (!VRStringUtil.isStringNullOrEmpty(mWord.getPosition())) {
                    mViewHolder.etPosition.setText(mWord.getPosition());

                }
                mViewHolder.etWordName.setHint(Define.WORD_INIT_NAME);
                mViewHolder.etMeaning.setHint(Define.WORD_INIT_DESCRIPTION);
                mViewHolder.etPronunciation.setHint(Define.WORD_INIT_PRONUNCIATION);
                mViewHolder.etPosition.setHint(Define.WORD_INIT_POSITION);
            }
            mViewHolder.etWordName.requestFocus();
            //showKeyboard();
        }
        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //hideKeyboard();
    }

    public boolean checkVoiceRecognition() {
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            return false;
        }
        return true;
    }

    public void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.pronunciation_you_can));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (textMatchList.size() > 0) {
                String voiceInText = textMatchList.get(0);
                if (!voiceInText.isEmpty()) {
                    voiceInText = voiceInText.substring(0,1).toUpperCase() + voiceInText.substring(1);
                    mViewHolder.etWordName.setText("");
                    mViewHolder.etWordName.setText(String.valueOf(voiceInText));
                }
            }
        } else {
            Log.d(TAG, ">>>onActivityResult cannot recognize the voice");
        }

    }

    private void showKeyboard() {
        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyboard() {
        Log.d(TAG, ">>>hideKeyboard START");
        try {
            Activity activity = getActivity();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
                String wordName = mViewHolder.etWordName.getText().toString().trim();
                String wordPronun = mViewHolder.etPronunciation.getText().toString().trim();
                String wordMeaning = mViewHolder.etMeaning.getText().toString().trim();
                String wordPosition = mViewHolder.etPosition.getText().toString().trim();
                //Word name is empty
                if ("".equalsIgnoreCase(wordName)) {
                    Toast toast = Toast.makeText(getActivity(), R.string.word_is_empty, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                    getDialog().getWindow().getDecorView().animate().setDuration(50).translationX(50).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            FragmentDialogEditWord.this.getDialog().getWindow().getDecorView().animate().setDuration(50).translationX(0).start();
                        }
                    }).start();
                    return;
                }
                mWord.setWordName(wordName);
                mWord.setPronunciation(wordPronun);
                mWord.setDefault_Meaning(wordMeaning);
                mWord.setPosition(wordPosition);
                mWord.setMp3Url(mViewHolder.mp3Url);
                OALBLL bl = new OALBLL(view.getContext());
                if (mWord.getWordId() == -1) {
                    bl.addNewWord(mWord);
                    if (mOnCreateOrUpdateWodListener != null) {
                        mOnCreateOrUpdateWodListener.onSave(mWord);
                    }
                } else {
                    bl.updateWord(mWord);
                    if (mOnCreateOrUpdateWodListener != null) {
                        mOnCreateOrUpdateWodListener.onUpdate(mWord);
                    }
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_get_info:
                if (((MainActivity)getActivity()).isOnline()) {
                    mLoading.setVisibility(View.VISIBLE);
                    mBtnGetInfo.setVisibility(View.INVISIBLE);
                    getInfo();
                } else {
                    showToast(R.string.you_are_offline);
                }
                break;
            case R.id.btn_voice:
                speak();
                break;
        }
    }

    private void getInfo() {
        HttpUtil.OnFinishLoadWordDefine onloadFinish = new HttpUtil.OnFinishLoadWordDefine() {
            @Override
            public void onFinishLoad(Document doc) {
                mLoading.setVisibility(View.INVISIBLE);
                mBtnGetInfo.setVisibility(View.VISIBLE);

                ArrayList<WordEntity> listEntryWord = HttpUtil.getWordInfo(doc);

                if (getActivity() != null) {
                    if (listEntryWord.size() <= 0) {
                        FragmentSuggestion fs = new FragmentSuggestion();
                        SparseArray<String> array = new SparseArray<>();
                        for(int i = 0;i < 50; i++) {
                            array.put(i, "aaaa" + i);
                        }
                        fs.setArray(array);
                        fs.show(getActivity().getSupportFragmentManager(), "suggestions");
                    } else {
                        FragmentSuggestInfo infoFgm = new FragmentSuggestInfo();
                        infoFgm.setArrayList(listEntryWord);
                        infoFgm.show(getActivity().getSupportFragmentManager(), "suggestInfoTag");
                        infoFgm.setAcceptSuggestionListener(new VROnDismisSuggestInfoListener(mViewHolder));
                    }
                }
            }
        };
        HttpUtil.LoadWordDefine task = new HttpUtil.LoadWordDefine(mViewHolder.etWordName.getText().toString(), onloadFinish);
        task.execute();
    }

    private void showToast(final int strId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
            }
        });

    }

    public class ViewHolder {
        public EditText etWordName;
        public EditText etPronunciation;
        public EditText etMeaning;
        public EditText etSentence;
        public EditText etPosition;
        public String mp3Url;
    }
}
