package com.suryo.gamatechno.app.application;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.suryo.gamatechno.app.others.Constants;

import java.net.URISyntaxException;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private Socket mSocket;

    private final String DBName = "Gamatechno.db";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DBName)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);
        try {
            mSocket = IO.socket("http://167.99.66.123:2727");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
