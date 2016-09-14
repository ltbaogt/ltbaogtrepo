package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 14/09/2016.
 */
public class OnTouchDismissListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Intent intent = new Intent(Define.VOCA_ACTION_CLOSE_VOCA_REMINDER);
        view.getContext().sendBroadcast(intent);
        return true;
    }
}
