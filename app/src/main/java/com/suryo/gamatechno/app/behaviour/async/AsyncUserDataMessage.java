package com.suryo.gamatechno.app.behaviour.async;

import android.content.Context;
import android.os.AsyncTask;

import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.db.ConversationHelper;
import com.suryo.gamatechno.app.db.MessageHelper;
import com.suryo.gamatechno.app.model.Message;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

public class AsyncUserDataMessage extends AsyncTask<Object, Object, Object> {
    private Context context;
    private DialogLoader dialogLoader;
    private Response.OnAsyncListener onAsyncListener;

    public AsyncUserDataMessage(Context context, DialogLoader dialogLoader, Response.OnAsyncListener onAsyncListener) {
        this.context = context;
        this.dialogLoader = dialogLoader;
        this.onAsyncListener = onAsyncListener;
    }

    @Override
    protected Object doInBackground(final Object[] params) {
        WSResponseDataMessage wsResponseDataUser = (WSResponseDataMessage) params[0];
        List<Message> mUsers = wsResponseDataUser.result.data.messages;
        MessageHelper mUserHelper = new MessageHelper();
//        mUserHelper.delete();
        for (Message mUser : mUsers) mUserHelper.save(mUser);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        onAsyncListener.onPost(o);
    }
}