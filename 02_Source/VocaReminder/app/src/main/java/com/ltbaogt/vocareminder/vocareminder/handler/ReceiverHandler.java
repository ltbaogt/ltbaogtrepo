package com.ltbaogt.vocareminder.vocareminder.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 10/09/2016.
 */
public class ReceiverHandler extends Handler {

    Context mContext;
    public ReceiverHandler(Context ctx) {
        Log.d(Define.TAG, ">>>ReceiverHandler init");
        mContext = ctx;
    }
    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case Define.HANDLER_WHAT_AUTO_DISMISS:
                dismissReminder();
                break;
        }
    }

    private void dismissReminder() {
        if (mContext != null) {
            Intent intent = new Intent(Define.VOCA_ACTION_CLOSE_VOCA_REMINDER);
            mContext.sendBroadcast(intent);
            Log.d("ReceiverHandler", ">>>dismissReminder");
        }
    }
}
