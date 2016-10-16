package com.ltbaogt.vocareminder.vocareminder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.DatabaseCreatedListener;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

/**
 * Created by MyPC on 20/09/2016.
 */
public class SplashScreenActivity extends Activity {

    private static final String TAG = Define.TAG + "SplashScreenActivity";
    public static final int MESSAGE_WHAT_DATABASE_CREATED = 2;
    private DatabaseCreatedListener mOnDbCreated;

    public static class SplashHandler extends Handler {
        private Activity mActivity;
        private boolean isFirstStart;

        public SplashHandler(Activity ctx, boolean isfs) {
            mActivity = ctx;
            isFirstStart = isfs;
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == MESSAGE_WHAT_DATABASE_CREATED) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainActivity;
                        if (!isFirstStart) {
                            mainActivity = new Intent(mActivity.getApplicationContext(), MainActivity.class);
                        } else {
                            mainActivity = new Intent(mActivity.getApplicationContext(), FirstStartActivity.class);
                        }
                        if (mainActivity != null) {
                            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mActivity.getApplicationContext().startActivity(mainActivity);
                            mActivity.finish();
                            mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                        VRLog.d(TAG, ">>>onDatabaseCreated END");
                    }
                }, 1000);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);
        appLogoAmination();
        SharedPreferences sp = SplashScreenActivity.this.getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE);
        boolean isFirstStart = sp.getBoolean(Define.REF_FIRST_START, true);
        mOnDbCreated = new DatabaseCreatedListener(new SplashHandler(this, !isFirstStart));

    }

    private void appLogoAmination() {
        ImageView imgLogo = (ImageView) findViewById(R.id.app_logo);
        imgLogo.animate().alpha(1).setDuration(1000).start();
        AssetManager assetManager = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_BOLD);
        typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_REGILAR);
        TextView tvSlogan = (TextView) findViewById(R.id.slogan);
        tvSlogan.setTypeface(typeface);
        tvSlogan.animate().alpha(1).setDuration(1000).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(getApplicationContext());
                db.setOnDatabaseCreateCompleted(mOnDbCreated);
                VRLog.d(TAG, ">>>Initialize database START");
                db.openDatabase();
                VRLog.d(TAG, ">>>Initialize database END");
            }
        }).start();
    }
}
