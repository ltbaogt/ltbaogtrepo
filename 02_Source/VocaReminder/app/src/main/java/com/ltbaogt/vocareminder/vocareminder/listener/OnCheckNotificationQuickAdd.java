package com.ltbaogt.vocareminder.vocareminder.listener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.CompoundButton;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.activity.MainActivity;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 20/09/2016.
 */
public class OnCheckNotificationQuickAdd implements CompoundButton.OnCheckedChangeListener {
    public static final int NOTIFICATION_ID = 99199;
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Context ctx = compoundButton.getContext();
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (b) {
            Intent intent = new Intent(compoundButton.getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Define.EXTRA_QUICK_ADD, 1);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx,
                    NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(ctx)
                    .setContentTitle("VocaReminder")
                    .setContentText("Tap to add a new vocabulary")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_eye);
            Notification n;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                n = builder.build();
            } else {
                n = builder.getNotification();
            }
            n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(NOTIFICATION_ID, n);
        } else {
            notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }
}
