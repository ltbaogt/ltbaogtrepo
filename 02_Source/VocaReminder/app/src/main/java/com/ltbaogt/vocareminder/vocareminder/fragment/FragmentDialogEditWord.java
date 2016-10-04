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
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.HashMapItem;

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
    private EditText mEtWordName;
    private EditText mEtPronunciation;
    private EditText mEtMeaning;
    private EditText mEtSentence;
    private ImageView mBtnCancel;
    private ImageView mBtnSave;
    private ImageView mBtnGetInfo;
    private OnCreateOrUpdateWodListener mOnCreateOrUpdateWodListener;
    private ProgressBar mLoading;
    private ImageView mBtnVoice;

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
        if (checkVoiceRecognition()) {
            mBtnVoice = (ImageView) v.findViewById(R.id.btn_voice);
            mBtnVoice.setVisibility(View.VISIBLE);
            mBtnVoice.setOnClickListener(this);
        }


//        String btn1Title = b.getString(Define.POPUP_BUTTON_01, "--");
//        String btn2Title = b.getString(Define.POPUP_BUTTON_02, "--");
//        //mBtnCancel.setText(btn1Title);
//        mBtnSave.setText(btn2Title);
        if (b != null) {
            mEtWordName = (EditText) v.findViewById(R.id.et_name);
            mEtMeaning = (EditText) v.findViewById(R.id.et_meaning);
            mEtPronunciation = (EditText) v.findViewById(R.id.et_pronunciation);
            //Set title for dialog
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            //Get word instance
            mWord = b.getParcelable(Define.WORD_OBJECT_PARCELABLE);
            //Get popup type add new of update
            int popupType = b.getInt(Define.POPUP_TYPE, Define.POPUP_NEW_WORD);
            if (popupType == Define.POPUP_NEW_WORD) {
                mEtWordName.setHint(mWord.getWordName());
                mEtPronunciation.setHint(mWord.getPronunciation());
                mEtMeaning.setHint(mWord.getDefault_Meaning());
            } else {
                mEtWordName.setText(mWord.getWordName());
                if (TextUtils.isEmpty(mWord.getPronunciation())) {
                    mEtPronunciation.setHint(Define.WORD_INIT_PRONUNCIATION);
                } else {
                    mEtPronunciation.setText(mWord.getPronunciation());
                }

                if (TextUtils.isEmpty(mWord.getDefault_Meaning())) {
                    mEtMeaning.setHint(Define.WORD_INIT_DESCRIPTION);
                } else {
                    mEtMeaning.setText(mWord.getDefault_Meaning());
                }
            }
            mEtWordName.requestFocus();
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
        if (resultCode == getActivity().RESULT_OK) {
            ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (textMatchList.size() > 0) {
                String voiceInText = textMatchList.get(0);
                if (!voiceInText.isEmpty()) {
                    voiceInText = voiceInText.substring(0,1).toUpperCase() + voiceInText.substring(1);
                    mEtWordName.setText("");
                    mEtWordName.setText(String.valueOf(voiceInText));
                }
            }
        } else {
            Log.d(TAG, ">>>onActivityResult cannot recognize the voice");
        }

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
                String wordName = mEtWordName.getText().toString().trim();
                String wordPronun = mEtPronunciation.getText().toString().trim();
                String wordMeaning = mEtMeaning.getText().toString().trim();
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
//                    getDialog().getWindow().getDecorView().animate().translationX(-100).setDuration(2000).start();
                    return;
                }
                mWord.setWordName(wordName);
                mWord.setPronunciation(wordPronun);
                mWord.setDefault_Meaning(wordMeaning);
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
//        HttpUtil.OnFinishLoadWordDefine onloadFinish = new HttpUtil.OnFinishLoadWordDefine() {
//            @Override
//            public void onFinishLoad(Document doc) {
//                mLoading.setVisibility(View.INVISIBLE);
//                mBtnGetInfo.setVisibility(View.VISIBLE);
//
//                final String mp3Url = HttpUtil.getMp3Url(doc);
//                Log.d(TAG, ">>>onFinishLoad= " + mp3Url);
//                if (mp3Url != null) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                MediaPlayer mediaPlayer = new MediaPlayer();
//                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                                mediaPlayer.setDataSource(mp3Url);
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                    @Override
//                                    public void onCompletion(MediaPlayer mediaPlayer) {
//                                        mediaPlayer.stop();
//                                        mediaPlayer.release();
//                                    }
//                                });
//                            } catch (Exception e) {
//                                Log.d(TAG, ">>>onFinishLoad play mp3 error" + Log.getStackTraceString(e));
//                            }
//                        }
//                    }).start();
//                } else {
//                    showToast(R.string.word_not_found);
//                }
//
//                String pronun = HttpUtil.getPronunciation(doc);
//                if (pronun != null && mEtPronunciation != null) {
//                    mEtPronunciation.setText(pronun);
//                    mWord.setPronunciation(pronun);
//                } else {
//                    Toast toast = Toast.makeText(getActivity(), R.string.word_not_found, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP,0,0);
//                    toast.show();
//                }
//
//            }
//        };
//        HttpUtil.LoadWordDefine task = new HttpUtil.LoadWordDefine(mEtWordName.getText().toString(), onloadFinish);
//        task.execute();
        String pronun = "This is Pronun";
        ArrayList<HashMapItem> array = new ArrayList<>();
        HashMapItem pNext = null;
        for (int i = 0; i < 5; i++) {
            HashMapItem itemInfo = new HashMapItem();
            itemInfo.put(Define.EXTRA_INDEX, "0");
            itemInfo.put(Define.EXTRA_MEANING, "meaning " + i);
            array.add(itemInfo);
//            if (nextItem != null) {
//                itemInfo.setNextItem(nextItem);
//            } else {
//                nextItem = itemInfo;
//            }
            itemInfo.setNextItem(pNext);
            pNext = itemInfo;
        }

        FragmentSuggestInfo infoFgm = new FragmentSuggestInfo();
        infoFgm.setArrayList(array);
        infoFgm.setPronun(pronun);
        infoFgm.show(getActivity().getSupportFragmentManager(), "suggestInfoTag");
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
}
