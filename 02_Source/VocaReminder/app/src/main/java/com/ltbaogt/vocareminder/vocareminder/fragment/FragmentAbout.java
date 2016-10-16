package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 23/09/2016.
 */
public class FragmentAbout extends BaseFragment {
    private static final String TAG = Define.TAG + "FragmentAbout";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        Button btnShare = (Button) v.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickShareApp();
            }
        });

        Button btnRate = (Button) v.findViewById(R.id.btn_rate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String pathToOpenGp = Define.MARKET_URI + getContext().getPackageName();
                    Log.d(TAG, "open app with packageName is= " + pathToOpenGp);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pathToOpenGp)));
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        });
        return v;
    }

    private void onClickShareApp() {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        String shareBody = Define.MARKET_URL + getContext().getPackageName();
        intentShare.putExtra(Intent.EXTRA_SUBJECT, "Share this app");
        intentShare.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intentShare, getString(R.string.share_via)));
    }
}
