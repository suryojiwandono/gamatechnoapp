package com.suryo.gamatechno.app.behaviour;

import android.view.View;

import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.model.WSResponseLogin;

public class Response {

    public interface OnWSListenerLogin {
        void onStart();

        void onSuccess(WSResponseLogin wsResponseLogin);

        void onFailed(WSResponseLogin wsResponseLogin);

        void onError(String err);

        void onFinish();
    }

    public interface OnWSListenerDataConversation {
        void onStart();

        void onSuccess(WSResponseDataConversation wsResponseLogin);

        void onFailed(WSResponseDataConversation wsResponseLogin);

        void onError(String err);

        void onFinish();
    }

    public interface OnWSListenerDataUser {
        void onStart();

        void onSuccess(WSResponseDataUser wsResponseLogin);

        void onFailed(WSResponseDataUser wsResponseLogin);

        void onError(String err);

        void onFinish();
    }

    public interface OnWSListenerDataMessage {
        void onStart();

        void onSuccess(WSResponseDataMessage wsResponseDataMessage);

        void onFailed(WSResponseDataMessage wsResponseDataMessage);

        void onError(String err);

        void onFinish();
    }

    public interface OnActionLoginListener {
        void onSuccess(WSResponseLogin wsResponseLogin);

        void onFailed(WSResponseBad wsResponseBad);
    }

    public interface OnActionDataConversationListener {
        void onSuccess(WSResponseDataConversation wsResponseLogin);

        void onFailed(WSResponseBad wsResponseBad);
    }

    public interface OnActionDataUserListener {
        void onSuccess(WSResponseDataUser wsResponseLogin);

        void onFailed(WSResponseBad wsResponseBad);
    }

    public interface OnActionDataMessageListener {
        void onSuccess(WSResponseDataMessage wsResponseDataMessage);

        void onFailed(WSResponseBad wsResponseBad);
    }

    public interface OnAsyncListener {
        void onPost(Object output);
    }

    public interface OnButtonListener {
        void onClick(Object object);
    }


    public interface OnRecyclerItemClick {
        void onClick(View view, int position);
    }
    public interface OnReceiverAction {
        void onRefresh(Object object);
    }
}
