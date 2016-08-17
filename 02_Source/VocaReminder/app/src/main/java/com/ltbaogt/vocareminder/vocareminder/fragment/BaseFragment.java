package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;

/**
 * Created by My PC on 17/08/2016.
 */

public class BaseFragment extends Fragment {

    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        return null;
    }

    public MainActivity getParentActivity() {
        if (mContext instanceof MainActivity) {
            return ((MainActivity) mContext);
        }
        return null;
    }
}
