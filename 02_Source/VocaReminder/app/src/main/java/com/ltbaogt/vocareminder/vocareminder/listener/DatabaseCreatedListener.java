package com.ltbaogt.vocareminder.vocareminder.listener;

import android.os.Handler;

import com.ltbaogt.vocareminder.vocareminder.activity.SplashScreenActivity;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.utils.VRLog;

/**
 * Created by MyPC on 22/09/2016.
 */
public class DatabaseCreatedListener implements OALDatabaseOpenHelper.OnDatabaseCreateCompleted {

    private static final String TAG = Define.TAG + "DatabaseCreatedListener";
    private Handler mHandler;
    public DatabaseCreatedListener(Handler hdl) {
        mHandler = hdl;
    }
    @Override
    public void onDatabaseCreated() {
        VRLog.d(TAG, ">>>onDatabaseCreated START");
        if (mHandler != null) {
            mHandler.sendEmptyMessage(SplashScreenActivity.MESSAGE_WHAT_DATABASE_CREATED);
        }
    }
}
