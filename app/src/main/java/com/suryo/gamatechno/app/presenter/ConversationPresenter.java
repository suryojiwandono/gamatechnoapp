package com.suryo.gamatechno.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.WSConversation;
import com.suryo.gamatechno.app.contract.ConversationContract;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.others.Utility;

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View mView;
    private Context context;

    public ConversationPresenter(Context context, ConversationContract.View view) {
        this.context = context;
        this.mView = view;
    }

    @Override
    public void doGetData(String token, int page) {
        WSConversation wsConversation = new WSConversation((Activity) context, token);
        wsConversation.onConversations(page);
        wsConversation.setOnResponse(new Response.OnActionDataConversationListener() {
            @Override
            public void onSuccess(WSResponseDataConversation wsResponseLogin) {
                mView.getDataSuccess(wsResponseLogin);
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
