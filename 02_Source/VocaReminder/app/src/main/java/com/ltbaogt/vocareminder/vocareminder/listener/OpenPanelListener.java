package com.ltbaogt.vocareminder.vocareminder.listener;

import android.view.View;

import com.ltbaogt.vocareminder.vocareminder.R;

/**
 * Created by MyPC on 10/09/2016.
 */
public class OpenPanelListener implements OALGestureListener.OnOpenSettingPanel {
    View mReminderLayout;
    public OpenPanelListener(View reminder) {
        mReminderLayout = reminder;
    }
    @Override
    public void onOpenSettingPanel() {
        View v = mReminderLayout.findViewById(R.id.panel_setting);
        v.setAlpha(0);
        v.setVisibility(View.VISIBLE);
        v.animate().alpha(1).setDuration(1000).start();
    }
}
