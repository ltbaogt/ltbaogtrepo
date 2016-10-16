package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.adapter.SuggestionAdapter;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.DictionaryFactory;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.FetchContentDictionarySite;
import com.ltbaogt.vocareminder.vocareminder.backgroundtask.HttpUtil;
import com.ltbaogt.vocareminder.vocareminder.bean.WordEntity;
import com.ltbaogt.vocareminder.vocareminder.listener.VROnDismisSuggestInfoListener;
import com.ltbaogt.vocareminder.vocareminder.utils.Utils;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by MyPC on 09/10/2016.
 */
public class FragmentSuggestion extends DialogFragment {

    private SparseArray<String> mArraySuggestion;
    private String mWordName;
    private FragmentDialogEditWord.ViewHolder mWordInfoViewHolder;
    private RecyclerView mRecyclerView;
    private String mDictionaryType;

    public void setDictionaryType(String dictType) {
        mDictionaryType = dictType;
    }
    private SuggestionAdapter.OnSuggestionItemClicked mOnSuggestionItemClicked = new SuggestionAdapter.OnSuggestionItemClicked() {
        @Override
        public void onSuggestionItemClicked(String wordName) {
            if (Utils.isOnline(getContext())) {
                mRecyclerView.animate().alpha(0).setDuration(500).start();
                getInfo(wordName);
            } else {
                Utils.showToastAtTop(getContext(), R.string.you_are_offline);
            }
        }
    };

    public void setWordInfoViewHolder(FragmentDialogEditWord.ViewHolder holder) {
        mWordInfoViewHolder = holder;
    }

    public void setWordName(String str) {
        mWordName = str;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_popup_suggestions, container, true);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView searchFor = (TextView) v.findViewById(R.id.search_for);
        searchFor.setText(String.format(getString(R.string.sg_tittle), mWordName));

        SuggestionAdapter adapter = new SuggestionAdapter(mArraySuggestion);
        adapter.onSuggestionItemClicked = mOnSuggestionItemClicked;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.suggestion);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(adapter);

        return v;
    }

    public void setArray(SparseArray<String> a) {
        mArraySuggestion = a;
    }

    private void getInfo(String wordName) {
        DictionaryFactory dictionaryFactory = new DictionaryFactory();
        final FetchContentDictionarySite siteInstance = dictionaryFactory.getDictionaryTypeInstance(mDictionaryType);
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
                    } else {
                        Log.d("AAAA", "Not Found");
                        mRecyclerView.animate().alpha(1).start();
                    }
                }
            }
        };

        //Request http
        HttpUtil.LoadWordDefine task = new HttpUtil.LoadWordDefine(siteInstance.getRedirectSuggestionUrl(),
                wordName,
                onloadFinish);
        task.execute();
    }
}
