package com.suryo.gamatechno.app.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.suryo.gamatechno.app.behaviour.SyncBootReceiver;
import com.suryo.gamatechno.app.others.Constants;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;

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
            mSocket = IO.socket(Constants.baseUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Intent service = new Intent(getApplicationContext(), SyncBootReceiver.class);
        sendBroadcast(service);
    }

    public Socket getSocket() {
        return mSocket;
    }
}
