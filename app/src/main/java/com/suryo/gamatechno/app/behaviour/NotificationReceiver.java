package com.suryo.gamatechno.app.behaviour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

import com.suryo.gamatechno.app.others.NotificationUtils;

import java.util.Objects;

/**
 * Created by Suryo on 5/27/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String idInspection = intent.getStringExtra("userId");
        int fgNotif = intent.getIntExtra("fgNotif", 0);
        boolean isReply = intent.getBooleanExtra("isReply", false);

        if (isReply) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                String textReply = Objects.requireNonNull(remoteInput.getCharSequence(NotificationUtils.REPLY_KEY)).toString();

            }
        }

        NotificationUtils.closeNotification(context, fgNotif);
    }
}
