package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.CambrigeDictionarySite;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.FetchContentDictionarySite;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.HttpUtil;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.listener.VROnDismisSuggestInfoListener;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by MyPC on 09/10/2016.
 */
public class FragmentSuggestion extends DialogFragment {

    private LinearLayout mSuggestions;
    private LayoutInflater mInflater;
    private SparseArray<String> mArraySuggestion;
    private String mWordName;
    private ViewHolder mHolder;
    private FragmentDialogEditWord.ViewHolder mWordInfoViewHolder;

    public void setWordInfoViewHolder(FragmentDialogEditWord.ViewHolder holder) {
        mWordInfoViewHolder = holder;
    }

    public void setWordName(String str) {
        mWordName = str;
    }
    private View.OnClickListener mOnClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof String) {
                String suggestion = (String) view.getTag();
                getInfo(suggestion);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        View v = mInflater.inflate(R.layout.fragment_popup_suggestions, container, true);
        mHolder = new ViewHolder();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mHolder.searchFor = (TextView) v.findViewById(R.id.search_for);
        mHolder.searchFor.setText(String.format(getString(R.string.sg_tittle), mWordName));
        mSuggestions = (LinearLayout) v.findViewById(R.id.suggestions);
        createSuggestionItems(mArraySuggestion.size());
        return v;
    }

    public void setArray(SparseArray<String> a) {
        mArraySuggestion = a;
    }

    private void createSuggestionItems(int num) {
        if (mSuggestions == null) return;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        lp.setMargins(10, 0, 10, 10);
        for (int i = 0; i < num; i++) {
            mHolder.item = (LinearLayout) mInflater.inflate(R.layout.suggestion_item, mSuggestions, false);
            mHolder.stt = ((TextView) mHolder.item.findViewById(R.id.tv_stt));
            mHolder.stt.setText("" + (mArraySuggestion.keyAt(i) + 1));

            mHolder.suggestion = ((TextView) mHolder.item.findViewById(R.id.tv_suggestion));
            mHolder.suggestion.setText("" + mArraySuggestion.valueAt(i));
            mHolder.item.setTag(mArraySuggestion.valueAt(i));
            mHolder.item.setOnClickListener(mOnClickItem);
            mSuggestions.addView(mHolder.item, lp);
        }
    }

    private void getInfo(String wordName) {
        final FetchContentDictionarySite siteInstance = new CambrigeDictionarySite();
        //Request done
        HttpUtil.OnFinishLoadWordDefine onloadFinish = new HttpUtil.OnFinishLoadWordDefine() {
            @Override
            public void onFinishLoad(Document doc) {

                ArrayList<WordEntity> listEntryWord = siteInstance.getWordInfo(doc);

                if (getActivity() != null) {
                    if (listEntryWord.size() > 0) {
                        FragmentSuggestInfo infoFgm = new FragmentSuggestInfo();
                        infoFgm.setArrayList(listEntryWord);
                        infoFgm.show(getActivity().getSupportFragmentManager(), "suggestInfoTag");
                        infoFgm.setAcceptSuggestionListener(new VROnDismisSuggestInfoListener(mWordInfoViewHolder));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FragmentSuggestion.this.dismiss();
                            }
                        }, 1000);
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

    private class ViewHolder {
        LinearLayout item;
        TextView searchFor;
        TextView stt;
        TextView suggestion;
    }
}
