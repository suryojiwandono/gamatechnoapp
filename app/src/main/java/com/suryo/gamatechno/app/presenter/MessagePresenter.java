package com.suryo.gamatechno.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.WSMessage;
import com.suryo.gamatechno.app.connectivity.WSUsers;
import com.suryo.gamatechno.app.contract.MessageContract;
import com.suryo.gamatechno.app.contract.UserContract;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.model.WSResponseDataUser;

public class MessagePresenter implements MessageContract.Presenter {
    private MessageContract.View mView;
    private Context context;

    public MessagePresenter(Context context, MessageContract.View view) {
        this.context = context;
        this.mView = view;
    }

    @Override
    public void doGetData(String token, String toUserId, int page) {
        WSMessage wsUsers = new WSMessage((Activity) context, token);
        wsUsers.onMessage(token, toUserId, page);
        wsUsers.setOnResponse(new Response.OnActionDataMessageListener() {
            @Override
            public void onSuccess(WSResponseDataMessage wsResponseDataMessage) {
                if (wsResponseDataMessage != null) {
                    if (wsResponseDataMessage.result.status == WSResponseDataMessage._SUCCESS) {
                        mView.getDataSuccess(wsResponseDataMessage);
                    } else mView.getDataFailed(null);
                }
            }

            @Override
            public void onFailed(WSResponseBad wsResponseBad) {
                mView.getDataFailed(wsResponseBad);
            }
        });
    }

    public void detachView() {
        mView = null;
    }
}
