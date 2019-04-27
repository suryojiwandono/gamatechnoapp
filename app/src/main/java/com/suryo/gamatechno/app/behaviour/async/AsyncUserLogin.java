package com.suryo.gamatechno.app.behaviour.async;

import android.content.Context;
import android.os.AsyncTask;

import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseLogin;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AsyncUserLogin extends AsyncTask<Object, Object, Object> {
    private Context context;
    private DialogLoader dialogLoader;
    private Response.OnAsyncListener onAsyncListener;

    public AsyncUserLogin(Context context, DialogLoader dialogLoader, Response.OnAsyncListener onAsyncListener) {
        this.context = context;
        this.dialogLoader = dialogLoader;
        this.onAsyncListener = onAsyncListener;
    }

    @Override
    protected Object doInBackground(final Object[] params) {
        WSResponseLogin wsResponseLogin = (WSResponseLogin) params[0];
        UserLogin userLogin = wsResponseLogin.result.data;
        userLogin.status = 1;

        //Set up Realm
        Realm.init(context);
        UserLoginHelper userLoginHelper = new UserLoginHelper();

        userLoginHelper.save(userLogin);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        onAsyncListener.onPost(o);
    }
}