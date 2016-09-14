package com.ltbaogt.vocareminder.vocareminder.listener;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 14/09/2016.
 */
public class OnClickStartApp implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        {
                Intent intent = new Intent(Define.VOCA_ACTION_CLOSE_VOCA_REMINDER);
            view.getContext().sendBroadcast(intent);
            Intent mainScreen = new Intent(view.getContext(), MainActivity.class);
            mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            view.getContext().startActivity(mainScreen);
        }
    }
}
