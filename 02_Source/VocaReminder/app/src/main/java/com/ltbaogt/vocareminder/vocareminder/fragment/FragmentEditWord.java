package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ltbaogt.vocareminder.vocareminder.R;

/**
 * Created by My PC on 09/08/2016.
 */

public class FragmentEditWord extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        return v;
    }
}
