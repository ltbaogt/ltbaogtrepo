package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.adapter.DictionaryAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by My PC on 08/08/2016.
 */

public class FragmentMain extends Fragment {

    public static final String TAG = Define.TAG + "FragmentMain";
    RecyclerView mRecycler;
    ArrayList<Word> mArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_word, container, false);
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler);
        OALBLL bl = new OALBLL(getContext());
        mArrayList = bl.getAllWords();
        Log.d(TAG, ">>>onCreateView size= " + mArrayList.size());
        DictionaryAdapter da = new DictionaryAdapter(mArrayList);
        mRecycler.setAdapter(da);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(lm);
        return v;
    }
}
