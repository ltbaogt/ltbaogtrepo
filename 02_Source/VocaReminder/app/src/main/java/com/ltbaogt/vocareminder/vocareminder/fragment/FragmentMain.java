package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FragmentMain extends Fragment {

    public static final String TAG = Define.TAG + "FragmentMain";
    RecyclerView mRecycler;
    ArrayList<Word> mArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_word, container, false);

        OALBLL bl = new OALBLL(getContext());
        mArrayList = bl.getAllWordsOrderByName();
        Log.d(TAG, ">>>onCreateView size= " + mArrayList.size());
        if (mArrayList.size() > 0) {
            //Show Recyclerview
            mRecycler = (RecyclerView) v.findViewById(R.id.recycler);
            mRecycler.setVisibility(View.VISIBLE);
            DictionaryAdapter da = new DictionaryAdapter(getActivity(), mArrayList);
            mRecycler.setAdapter(da);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            mRecycler.setLayoutManager(lm);
        } else {
            v.findViewById(R.id.noword_layout).setVisibility(View.VISIBLE);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        }
        return v;
    }

    private void showDialog() {
        Activity a = getActivity();
        if (a != null && a instanceof MainActivity) {
            FragmentManager fm = ((MainActivity) a).getSupportFragmentManager();
            FragmentDialogEditWord editWordDialog = new FragmentDialogEditWord();
            editWordDialog.show(fm, "tag");

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setOnClickListener(null);
        Log.d(TAG, ">>>onDestroyView START");
    }
}
