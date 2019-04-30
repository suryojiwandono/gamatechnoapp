package com.suryo.gamatechno.app.behaviour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshReceiver extends BroadcastReceiver {
    private Response.OnReceiverAction onReceiverAction;

    public RefreshReceiver(Response.OnReceiverAction onReceiverAction) {
        this.onReceiverAction = onReceiverAction;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) onReceiverAction.onRefresh(action);
    }
}