package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.adapter.DictionaryAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.util.ArrayList;

/**
 * Created by My PC on 08/08/2016.
 */

public class FragmentMain extends BaseFragment {

    public static final String TAG = Define.TAG + "FragmentMain";
    private RecyclerView mRecycler;
    private ArrayList<Word> mArrayList;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mMainView = inflater.inflate(R.layout.fragment_list_word, container, false);
        OALBLL bl = new OALBLL(getContext());
        mArrayList = bl.getAllWordsOrderByName();
        Log.d(TAG, ">>>onCreateView size= " + mArrayList.size());
        //Show Recyclerview
        mRecycler = (RecyclerView) mMainView.findViewById(R.id.recycler);
        mRecycler.setVisibility(View.VISIBLE);
        DictionaryAdapter da = new DictionaryAdapter(getActivity(), mArrayList);
        mRecycler.setAdapter(da);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(lm);
        setRecyclerViewItemTouchListener();
        updateLayoutNoWord();
        return mMainView;
    }

    public void addNewWord(Word w) {
        mArrayList.add(w);
        mRecycler.getAdapter().notifyItemInserted(mArrayList.indexOf(w));
        updateLayoutNoWord();
    }

    public void updateWord(Word w) {
        Log.d(TAG, ">>>updateWord START");
        int idWord = mArrayList.indexOf(w);
        mArrayList.set(idWord, w);
        mRecycler.getAdapter().notifyItemChanged(idWord);
        //updateLayoutNoWord();
        Log.d(TAG, ">>>updateWord END");
    }

    public void updateLayoutNoWord() {
        Log.d(TAG, ">>>updateLayoutNoWord START");
        if (mMainView != null && mArrayList != null && mArrayList.size() <= 0 && mMainView != null) {
            mMainView.findViewById(R.id.noword_layout).setVisibility(View.VISIBLE);
            if (mRecycler != null) {
                mRecycler.setVisibility(View.INVISIBLE);
            }
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity = FragmentMain.this.getParentActivity();
                    if (activity != null) {
                        Word w = new Word();
                        activity.showDialogNewWord(w);
                    }
                }
            };
            mMainView.setOnClickListener(listener);
        } else {
            if (mRecycler != null) {
                mRecycler.setVisibility(View.VISIBLE);
                mMainView.findViewById(R.id.noword_layout).setVisibility(View.INVISIBLE);
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
                Word w = mArrayList.get(position);
                OALBLL bl = new OALBLL(FragmentMain.this.getContext());
                bl.deleteWordById(w.getWordId());
                mArrayList.remove(position);
                mRecycler.getAdapter().notifyItemRemoved(position);
                updateLayoutNoWord();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecycler);
    }
}
