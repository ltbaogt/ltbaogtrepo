package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.R;

import java.lang.ref.WeakReference;

/**
 * Created by MyPC on 10/09/2016.
 */
public class OpenPanelListener implements OALGestureListener.OnOpenSettingPanel {
    private WeakReference<View> mReminderLayout;
    public OpenPanelListener(View reminder) {
        mReminderLayout = new WeakReference<>(reminder);
    }
    @Override
    public void onOpenSettingPanel() {
        if (mReminderLayout != null && mReminderLayout.get()!=null){
            View v = mReminderLayout.get().findViewById(R.id.panel_setting);
            v.setAlpha(0);
            v.setVisibility(View.VISIBLE);
            v.animate().alpha(1).setDuration(1000).start();
        }
    }
}
