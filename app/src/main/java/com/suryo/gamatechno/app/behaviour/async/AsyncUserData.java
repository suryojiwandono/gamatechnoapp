package com.suryo.gamatechno.app.behaviour.async;

import android.content.Context;
import android.os.AsyncTask;

import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.db.MUserHelper;
import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.model.WSResponseLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AsyncUserData extends AsyncTask<Object, Object, Object> {
    private Response.OnAsyncListener onAsyncListener;
    private int page;

    public AsyncUserData(int page, Response.OnAsyncListener onAsyncListener) {
        this.page = page;
        this.onAsyncListener = onAsyncListener;
    }

    @Override
    protected Object doInBackground(final Object[] params) {
        WSResponseDataUser wsResponseDataUser = (WSResponseDataUser) params[0];
        List<MUser> mUsers = wsResponseDataUser.result.data.users;
        MUserHelper mUserHelper = new MUserHelper();
//        mUserHelper.delete();
        for (MUser mUser : mUsers) {
            mUser.page = page;
            mUserHelper.save(mUser);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        onAsyncListener.onPost(o);
    }
}