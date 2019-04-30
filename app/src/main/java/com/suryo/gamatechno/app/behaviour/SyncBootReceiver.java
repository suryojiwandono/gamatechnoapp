package com.suryo.gamatechno.app.behaviour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.suryo.gamatechno.app.others.Constants;

/**
 * Created by Suryo on 5/27/2016.
 */
public class SyncBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SyncService.class);
        service.setAction(Constants.START_FG_BG_SYNC);
        context.startService(service);
    }
}
