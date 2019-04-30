package com.suryo.gamatechno.app.behaviour;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.suryo.gamatechno.app.application.MyApplication;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.SocketRoom;
import com.suryo.gamatechno.app.model.SocketRoomChat;
import com.suryo.gamatechno.app.model.SocketRoomDetail;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.others.NotificationUtils;
import com.suryo.gamatechno.app.others.Utility;

import java.util.Objects;

/**
 * Created by Suryo on 5/27/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int fgNotif = intent.getIntExtra("fgNotif", 0);
        boolean isReply = intent.getBooleanExtra("isReply", false);

        if (isReply) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                String textReply = Objects.requireNonNull(remoteInput.getCharSequence(NotificationUtils.REPLY_KEY)).toString();
                if (!textReply.isEmpty()) {
                    MyApplication myApplication = (MyApplication) context.getApplicationContext();
                    Socket mSocket = myApplication.getSocket();
                    if (mSocket.connected()) {
                        UserLoginHelper userLoginHelper = new UserLoginHelper();
                        SocketRoomChat socketRoomChat = new SocketRoomChat();
                        socketRoomChat.userId = userLoginHelper.getUserLogin().userId;
                        socketRoomChat.message = textReply;
                        SocketRoomDetail socketRoomDetail = new SocketRoomDetail();
                        socketRoomDetail.data = socketRoomChat;
                        SocketRoom socketRoom = new SocketRoom();
                        socketRoom.request = socketRoomDetail;

                        Gson gson = new Gson();
                        mSocket.emit("send message", gson.toJson(socketRoom));
                    } else {
                        Utility.Toast(context, "Socket Disconnected");
                    }
                }
            }
        }

        NotificationUtils.closeNotification(context, fgNotif);
    }
}
