package com.ltbaogt.vocareminder.vocareminder.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.BuildConfig;
import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

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
                    VRLog.d(TAG, "open app with packageName is= " + pathToOpenGp);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pathToOpenGp)));
                } catch (Exception e) {
                    VRLog.e(TAG, e);
                }
            }
        });

        ImageView fbLogo = (ImageView) v.findViewById(R.id.fb_logo);
        fbLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   startActivity(startFacebook());
                } catch(Exception e) {

                }
            }
        });

        ImageView gmLogo = (ImageView) v.findViewById(R.id.gm_logo);
        gmLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGmail();
            }
        });

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        TextView tvVersion = (TextView) v.findViewById(R.id.tv_version);
        tvVersion.setText(String.format(getString(R.string.version_string),versionName, versionCode));
        return v;
    }

    private Intent startFacebook() {
        try {
           return new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.fb_open_uri)));
        } catch (Exception e) {

        }
        Uri uri = Uri.parse(getString(R.string.fb_open_url));
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    private void startGmail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {getString(R.string.developer_email)});
        startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
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
