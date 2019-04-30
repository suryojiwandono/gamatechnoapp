package com.suryo.gamatechno.app.behaviour;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.suryo.gamatechno.app.application.MyApplication;
import com.suryo.gamatechno.app.db.RoomHelper;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.Room;
import com.suryo.gamatechno.app.model.WSResponseDataRoom;
import com.suryo.gamatechno.app.others.Constants;
import com.suryo.gamatechno.app.others.NotificationUtils;
import com.suryo.gamatechno.app.others.Utility;

import java.util.Map;

/**
 * Created by Suryo on 5/27/2016.
 */
public class SyncService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent);
        return START_STICKY;
    }

    private void onStart(Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(Constants.START_FG_BG_SYNC)) {
                if (!Utility.isServiceRunning(this, Constants.START_FG_BG_SYNC)) {
                    onLoad();
                }
            } else if (intent.getAction().equals(Constants.STOP_FG_BG_SYNC)) {
                stopSelf();
            }
        }
    }

    private Socket mSocket;

    private void onLoad() {
        UserLoginHelper userLoginHelper = new UserLoginHelper();
        if (userLoginHelper.isLogin()) {
            if (mSocket != null) {
                if (!mSocket.connected()) {
                    Utility.Logs.i("FINISHED");
                    mSocket.connect();
                } else {
                    Utility.Logs.i("sync bg");
                }
            } else {
                MyApplication myApplication = (MyApplication) getApplication();
                mSocket = myApplication.getSocket();
                mSocket.on(Socket.EVENT_CONNECT, onConnect);
                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.on("received message", onNewMessage);
                mSocket.io().on(Manager.EVENT_TRANSPORT, onEvenTransport);
                mSocket.connect();
            }
        }
    }

    private Emitter.Listener onConnect = args -> Utility.Logs.v("CONNECTED==============");

    private Emitter.Listener onDisconnect = args -> {
        Utility.Logs.v("DISCONNECTED==============");
    };

    private Emitter.Listener onConnectError = args -> Utility.Logs.v("ERROR:==============>" + args[0].toString());

    private Emitter.Listener onNewMessage = args -> {
        Utility.Logs.v("NEW MESSAGE");
        Utility.Logs.v("======>" + args[0].toString());
        Gson gson = new Gson();
        WSResponseDataRoom wsResponseDataRoom = gson.fromJson(args[0].toString(), WSResponseDataRoom.class);
        RoomHelper roomHelper = new RoomHelper();
        Room room = wsResponseDataRoom.result.data;
        room.isRead = false;
        Utility.Logs.v("====>"+room.fromUserEmail);
        try {
            if (!roomHelper.isAlready(room)) {
                roomHelper.save(room);
                NotificationUtils.showNotification(getApplicationContext());
                sendBroadcast(new Intent(Constants.REFRESH_MESSAGE));
            }
        } catch (JsonSyntaxException e) {
        }
    };

    private Emitter.Listener onEvenTransport = args -> {
        Transport transport = (Transport) args[0];
        transport.on(Transport.EVENT_REQUEST_HEADERS, args1 -> {
            @SuppressWarnings("unchecked")
            Map<String, String> headers = (Map<String, String>) args1[0];
            UserLoginHelper userLoginHelper = new UserLoginHelper();
            String token = userLoginHelper.getUserLogin().token;
//            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VyX2lkIjoyLCJyb2xlIjoiVFJBVkVMTEVSIiwiY29kZSI6IjI0NzA4ZWQ5Y2UxMCIsImlhdCI6MTU1NjE4NDQxOSwiZXhwIjoxNTU3Mzk0MDE5LCJpc3MiOiJsb2NhbGhvc3QifQ.p44i2wikFOxxTkH5mNIagj47jdCtQwHY93fVkfHByuGiYVWBr4R-qBZOMiQvKH87NLpDYdwsoNMbKrbXaP9RVQ";
            headers.put("token", token);
        });
    };

    /* restart service when apps killed */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setAction(Constants.START_FG_BG_SYNC);
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 2, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);
    }


}
