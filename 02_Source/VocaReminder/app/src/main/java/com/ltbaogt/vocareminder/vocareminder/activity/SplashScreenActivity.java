package com.ltbaogt.vocareminder.vocareminder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.listener.DatabaseCreatedListener;

/**
 * Created by MyPC on 20/09/2016.
 */
public class SplashScreenActivity extends Activity {

    public static final int MESSAGE_WHAT_CREATE_DATABASE = 1;
    public static final int MESSAGE_WHAT_DATABASE_CREATED = 2;
    private SplashHandler mHandler;
    private DatabaseCreatedListener mOnDbCreated;

    public static class SplashHandler extends Handler {
        private Activity mActivity;
        private DatabaseCreatedListener mDatabaseCreatedListener;

        public SplashHandler(Activity ctx) {
            mActivity = ctx;

        }

        public void setDatabaseCreatedListener(DatabaseCreatedListener listener) {
            mDatabaseCreatedListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == MESSAGE_WHAT_DATABASE_CREATED) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainActivity = new Intent(mActivity.getApplicationContext(), MainActivity.class);
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mActivity.getApplicationContext().startActivity(mainActivity);
                        mActivity.finish();
                        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 500);
            } else if (what == MESSAGE_WHAT_CREATE_DATABASE) {
                OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(mActivity.getApplicationContext());
                db.setOnDatabaseCreateCompleted(mDatabaseCreatedListener);
                db.openDatabase();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);
        AssetManager assetManager = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_BOLD);
        TextView tvName = (TextView) findViewById(R.id.app_name);
        tvName.setTypeface(typeface);
        typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_REGILAR);
        TextView tvSlogan = (TextView) findViewById(R.id.slogan);
        tvSlogan.setTypeface(typeface);
        mHandler = new SplashHandler(this);
        mOnDbCreated = new DatabaseCreatedListener(mHandler);
        mHandler.setDatabaseCreatedListener(mOnDbCreated);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(MESSAGE_WHAT_CREATE_DATABASE);
    }
}
