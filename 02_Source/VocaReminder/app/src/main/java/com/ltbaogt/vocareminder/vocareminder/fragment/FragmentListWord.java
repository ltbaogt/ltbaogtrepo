package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.adapter.DictionaryAdapter;
import com.ltbaogt.vocareminder.vocareminder.adapter.TagAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.Tag;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.bean.WordProvider;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.OALSimpleOnGestureListener;
import com.ltbaogt.vocareminder.vocareminder.provider.AppContact;
import com.ltbaogt.vocareminder.vocareminder.provider.ProviderWrapper;

import java.util.ArrayList;

/**
 * Created by My PC on 08/08/2016.
 */

public class FragmentListWord extends BaseFragment {

    public static final String TAG = Define.TAG + "FragmentListWord";
    private RecyclerView mRecycler;
    private RecyclerView mRecyclerTag;
    private View mMainView;
    private LinearLayout mTagPanel;

    private OALSimpleOnGestureListener mTagPanelSimpleOnGestureListener;

    private static class OnDoubleTapTagPanel implements OALSimpleOnGestureListener.OnDoubleTap {

        FragmentListWord mFragmentListWord;

        public OnDoubleTapTagPanel(FragmentListWord listWord) {
            mFragmentListWord = listWord;
        }

        @Override
        public void onDoubleTap() {
            if (mFragmentListWord != null) {
                mFragmentListWord.toggleTagPanel();
            }
        }
    }

    private GestureDetector mTagPanelGestureDetector;

    private OnDoubleTapTagPanel mOnDoubleTapTagPanel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView START");
        mMainView = inflater.inflate(R.layout.fragment_list_word, container, false);
        ProviderWrapper pw = new ProviderWrapper(getContext());
        ArrayList<Word> arrayList = pw.getListWord();
        //Show Recyclerview
        mRecycler = (RecyclerView) mMainView.findViewById(R.id.recycler);
        mRecycler.setVisibility(View.VISIBLE);
        DictionaryAdapter da = new DictionaryAdapter(getActivity(), arrayList);
        mRecycler.setAdapter(da);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(lm);
        setRecyclerViewItemTouchListener();
        updateLayoutNoWord();

        //Remve Tag panel start
//        ArrayList<Tag> tagList = bl.getTagList();
//        mRecyclerTag = (RecyclerView) mMainView.findViewById(R.id.recycler_tag);
//        TagAdapter tagAdapter = new TagAdapter(tagList);
//        RecyclerView.LayoutManager lmTag = new LinearLayoutManager(getContext());
//        mRecyclerTag.setLayoutManager(lmTag);
//        mRecyclerTag.setAdapter(tagAdapter);
//
//
//        mTagPanel = (LinearLayout) mMainView.findViewById(R.id.panel_tag);
//        mOnDoubleTapTagPanel = new OnDoubleTapTagPanel(this);
//        mTagPanelSimpleOnGestureListener = new OALSimpleOnGestureListener();
//        mTagPanelSimpleOnGestureListener.setOnDoubleTapListener(mOnDoubleTapTagPanel);
//        mTagPanelGestureDetector = new GestureDetector(getActivity(), mTagPanelSimpleOnGestureListener);
//        mTagPanel.setClickable(true);
//        mTagPanel.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return mTagPanelGestureDetector.onTouchEvent(motionEvent);
//            }
//        });
//Remve Tag panel end
        Log.d(TAG, "onCreateView END");
        return mMainView;
    }

    //Get Adapter of RecyclerView. Current is DictionaryAdapter instance
    public DictionaryAdapter getWordAdapter() {
        return (DictionaryAdapter) mRecycler.getAdapter();
    }

    public void addNewWord(Word w) {
        getWordAdapter().insertWord(w);
        updateLayoutNoWord();
    }

    public void filterRawWords() {
        getWordAdapter().filterRawWords();
    }

    public void noFilter() {
        getWordAdapter().noFilter();
    }

    public void updateWord(Word w) {
        Log.d(TAG, ">>>updateWord START");
        getWordAdapter().updateWord(w);
        Log.d(TAG, ">>>updateWord END");
    }

    public void updateLayoutNoWord() {
        Log.d(TAG, ">>>updateLayoutNoWord START");
        View btnAdd = mMainView.findViewById(R.id.noword_layout);
        if (getWordAdapter() != null && getWordAdapter().getItemCount() <= 0) {

            btnAdd.setVisibility(View.VISIBLE);
            if (mRecycler != null) {
                mRecycler.setVisibility(View.INVISIBLE);
            }
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity = FragmentListWord.this.getParentActivity();
                    if (activity != null) {
                        Word w = new Word();
                        activity.showDialogNewWord(w);
                    }
                }
            };
            btnAdd.setOnClickListener(listener);
        } else {
            if (mRecycler != null) {
                mRecycler.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.INVISIBLE);
            }
        }
        Log.d(TAG, ">>>updateLayoutNoWord END");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setOnClickListener(null);
        Log.d(TAG, ">>>onDestroyView START");
    }

    private void setRecyclerViewItemTouchListener() {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word w = getWordAdapter().removeWord(position);
                OALBLL bl = new OALBLL(FragmentListWord.this.getContext());
                bl.deleteWordById(w.getWordId());
                updateLayoutNoWord();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecycler);
    }

    //Show Tag panel
    public void toggleTagPanel() {
        if (mTagPanel != null) {
            if (mTagPanel.getVisibility() == View.VISIBLE) {
                mTagPanel.setVisibility(View.GONE);
            } else {
                mTagPanel.setVisibility(View.VISIBLE);
            }
        }
    }
    //Show Tag panel
    public void hideTagPanel() {
        if (mTagPanel != null) {
            mTagPanel.setVisibility(View.GONE);
        }
    }
}
