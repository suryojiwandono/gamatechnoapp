package com.suryo.gamatechno.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.WSUsers;
import com.suryo.gamatechno.app.contract.UserContract;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataUser;

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View mView;
    private Context context;

    public UserPresenter(Context context, UserContract.View view) {
        this.context = context;
        this.mView = view;
    }

    @Override
    public void doGetData(String token, int page) {
        WSUsers wsUsers = new WSUsers((Activity) context, token);
        wsUsers.onUsers();
        wsUsers.setOnResponse(new Response.OnActionDataUserListener() {
            @Override
            public void onSuccess(WSResponseDataUser wsResponseLogin) {
                if (wsResponseLogin != null) {
                    if (wsResponseLogin.result.status == WSResponseDataConversation._SUCCESS) {
                        mView.getDataSuccess(wsResponseLogin);
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
