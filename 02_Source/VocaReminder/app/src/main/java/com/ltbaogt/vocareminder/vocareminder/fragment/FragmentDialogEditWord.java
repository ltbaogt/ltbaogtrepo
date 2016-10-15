package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.CambrigeDictionarySite;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.DictionaryFactory;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.EnViDictionarySite;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.FetchContentDictionarySite;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.HttpUtil;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.VROnDismisSuggestInfoListener;
import com.ltbaogt.vocareminder.vocareminder.utils.VRStringUtil;

import org.jsoup.nodes.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by My PC on 10/08/2016.
 */

public class FragmentDialogEditWord extends DialogFragment implements View.OnClickListener {

    private static final String TAG = Define.TAG + "FragmentDialogEditWord";
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    private Word mWord;
    private ImageView mBtnGetInfo;
    private OnCreateOrUpdateWodListener mOnCreateOrUpdateWodListener;
    private ProgressBar mLoading;

    private ViewHolder mViewHolder;

    private String mDictionaryType;

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
        ImageView btnCancel = (ImageView) v.findViewById(R.id.btn_cancel);
        ImageView btnSave = (ImageView) v.findViewById(R.id.btn_save);
        mBtnGetInfo = (ImageView) v.findViewById(R.id.btn_get_info);
        mLoading = (ProgressBar) v.findViewById(R.id.ctrlActivityIndicator);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        mBtnGetInfo.setOnClickListener(this);
        mViewHolder = new ViewHolder();
        if (checkVoiceRecognition()) {
            ImageView btnVoice = (ImageView) v.findViewById(R.id.btn_voice);
            btnVoice.setVisibility(View.VISIBLE);
            btnVoice.setOnClickListener(this);
        }

        if (b != null) {
            mViewHolder.etWordName = (EditText) v.findViewById(R.id.et_name);
            mViewHolder.etMeaning = (EditText) v.findViewById(R.id.et_meaning);
            mViewHolder.etPronunciation = (EditText) v.findViewById(R.id.et_pronunciation);
            mViewHolder.etPosition = (EditText) v.findViewById(R.id.et_position);
            mViewHolder.mButtonSearchMeaning = v.findViewById(R.id.btn_play_pronun);

            setupDictionaryType(mBtnGetInfo);
            mBtnGetInfo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d(TAG, ">>>onLongClick change language");
                    openPopupMenu(mBtnGetInfo);
                    return true;
                }
            });
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

    private void setupDictionaryType(ImageView view) {
        if (view == null) return;
        SharedPreferences sp = view.getContext().getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE);
        String type = sp.getString(Define.REF_DICTIONARY_TYPE,null);
        if (Define.REF_DICTIONARY_TYPE_EN.equals(type)) {
            view.setBackgroundResource(R.drawable.en_dict);
            mDictionaryType = Define.REF_DICTIONARY_TYPE_EN;
        } else {
            view.setBackgroundResource(R.drawable.vi_dict);
            mDictionaryType = Define.REF_DICTIONARY_TYPE_VI;
        }
    }

    private void openPopupMenu(final ImageView anchorView) {
        // inflate menu
        final PopupMenu popup = new PopupMenu(anchorView.getContext(), anchorView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.change_language_popup_menu_layout, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences sp = anchorView.getContext().getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                switch (item.getItemId()) {
                    case R.id.action_language_vi:
                        mDictionaryType = Define.REF_DICTIONARY_TYPE_VI;
                        break;
                    default:
                        mDictionaryType = Define.REF_DICTIONARY_TYPE_EN;
                        break;
                }
                editor.putString(Define.REF_DICTIONARY_TYPE, mDictionaryType);
                editor.apply();
                setupDictionaryType(mBtnGetInfo);
                popup.dismiss();
                return true;
            }
        });
        try {
            //This block forces popup menu shows icons
            Field f = popup.getClass().getDeclaredField("mPopup");
            f.setAccessible(true);
            MenuPopupHelper mph = (MenuPopupHelper) f.get(popup);
            mph.setForceShowIcon(true);
        } catch (Exception e) {

        }
        popup.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //hideKeyboard();
    }

    public boolean checkVoiceRecognition() {
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
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
                    VRStringUtil.showToastAtTop(getContext(), R.string.word_is_empty);
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
                if (VRStringUtil.isOnline(getContext())) {
                    mLoading.setVisibility(View.VISIBLE);
                    mBtnGetInfo.setVisibility(View.INVISIBLE);
                    getInfo(mViewHolder.etWordName.getText().toString());
                } else {
                    VRStringUtil.showToastAtTop(getContext(), R.string.you_are_offline);
                }
                break;
            case R.id.btn_voice:
                speak();
                break;
        }
    }

    private void getInfo(String wordName) {
        DictionaryFactory dictFactory = new DictionaryFactory();
        final FetchContentDictionarySite siteInstance = dictFactory.getDictionaryTypeInstance(mDictionaryType);
        //Request done
        HttpUtil.OnFinishLoadWordDefine onloadFinish = new HttpUtil.OnFinishLoadWordDefine() {
            @Override
            public void onFinishLoad(Document doc) {
                mLoading.setVisibility(View.INVISIBLE);
                mBtnGetInfo.setVisibility(View.VISIBLE);
                if (doc == null) {
                    VRStringUtil.showToastAtBottom(getContext(), R.string.cannot_get_information);
                    return;
                }
                ArrayList<WordEntity> listEntryWord = siteInstance.getWordInfo(doc);

                if (getActivity() != null) {
                    if (listEntryWord.size() <= 0) {
                        getSuggestions();
                    } else {
                        FragmentSuggestInfo infoFgm = new FragmentSuggestInfo();
                        infoFgm.setArrayList(listEntryWord);
                        infoFgm.show(getActivity().getSupportFragmentManager(), "suggestInfoTag");
                        infoFgm.setAcceptSuggestionListener(new VROnDismisSuggestInfoListener(mViewHolder));
                    }
                }
            }
        };

        //Request http
        HttpUtil.LoadWordDefine task = new HttpUtil.LoadWordDefine(siteInstance.getUrl(),
                wordName,
                onloadFinish);
        task.execute();
    }

    private void getSuggestions() {

        DictionaryFactory dictionaryFactory = new DictionaryFactory();
        final FetchContentDictionarySite siteInstance = dictionaryFactory.getDictionaryTypeInstance(mDictionaryType);
        //Request done
        HttpUtil.OnFinishLoadWordDefine onloadFinish = new HttpUtil.OnFinishLoadWordDefine() {
            @Override
            public void onFinishLoad(Document doc) {
                if (getActivity() != null) {
                    mLoading.setVisibility(View.INVISIBLE);
                    mBtnGetInfo.setVisibility(View.VISIBLE);
                    SparseArray<String> array = siteInstance.getSuggestions(doc);
                    FragmentSuggestion fs = new FragmentSuggestion();
                    fs.setWordInfoViewHolder(mViewHolder);
                    fs.setWordName(mViewHolder.etWordName.getText().toString());
                    fs.setArray(array);
                    fs.setDictionaryType(mDictionaryType);
                    fs.show(getActivity().getSupportFragmentManager(), "suggestions_2");
                }
            }
        };

        //Request http
        HttpUtil.LoadWordDefine task = new HttpUtil.LoadWordDefine(siteInstance.getSuggestionUrl(),
                mViewHolder.etWordName.getText().toString(),
                onloadFinish);
        task.execute();
    }

    public class ViewHolder {
        public EditText etWordName;
        public EditText etPronunciation;
        public EditText etMeaning;
        public EditText etPosition;
        public String mp3Url;
        public View mButtonSearchMeaning;
    }
}
