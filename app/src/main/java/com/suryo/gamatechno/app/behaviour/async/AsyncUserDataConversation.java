package com.suryo.gamatechno.app.behaviour.async;

import android.content.Context;
import android.os.AsyncTask;

import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.db.ConversationHelper;
import com.suryo.gamatechno.app.db.MUserHelper;
import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

public class AsyncUserDataConversation extends AsyncTask<Object, Object, Object> {
    private Context context;
    private DialogLoader dialogLoader;
    private Response.OnAsyncListener onAsyncListener;

    public AsyncUserDataConversation(Context context, DialogLoader dialogLoader, Response.OnAsyncListener onAsyncListener) {
        this.context = context;
        this.dialogLoader = dialogLoader;
        this.onAsyncListener = onAsyncListener;
    }

    @Override
    protected Object doInBackground(final Object[] params) {
        WSResponseDataConversation wsResponseDataUser = (WSResponseDataConversation) params[0];
        List<TConversation> mUsers = wsResponseDataUser.result.data.tConversations;
        ConversationHelper mUserHelper = new ConversationHelper();
//        mUserHelper.delete();
        for (TConversation mUser : mUsers) mUserHelper.save(mUser);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        onAsyncListener.onPost(o);
    }
}