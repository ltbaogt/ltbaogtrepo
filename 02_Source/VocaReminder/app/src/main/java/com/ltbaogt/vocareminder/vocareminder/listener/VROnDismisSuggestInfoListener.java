package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.DialogInterface;

import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentDialogEditWord.ViewHolder;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentSuggestInfo;

/**
 * Created by MyPC on 08/10/2016.
 */
public class VROnDismisSuggestInfoListener implements FragmentSuggestInfo.AcceptSuggestionListener {

    private ViewHolder mViewHolder;
    public VROnDismisSuggestInfoListener(ViewHolder vHolder) {
        mViewHolder = vHolder;
    }

    @Override
    public void onDismiss(WordEntity entity) {
        if (mViewHolder != null && entity != null) {
            mViewHolder.etWordName.setText(entity.wordName);
            mViewHolder.etPronunciation.setText(entity.pronunciation);
            mViewHolder.etMeaning.setText(entity.getSelectedMeaning());
            mViewHolder.etPosition.setText(entity.position);
            mViewHolder.mp3Url = entity.mp3URL;
        }
    }
}
