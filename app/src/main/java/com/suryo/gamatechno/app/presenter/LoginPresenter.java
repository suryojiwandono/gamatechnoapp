package com.suryo.gamatechno.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.WSLogin;
import com.suryo.gamatechno.app.contract.LoginContract;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseLogin;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private Context context;

    public LoginPresenter(Context context, LoginContract.View view) {
        this.context = context;
        this.mView = view;
    }

    @Override
    public void doLogin(String username, String password) {
        WSLogin wsLogin = new WSLogin((Activity) context);
        wsLogin.onLogin(username, password);
        wsLogin.setOnResponse(new Response.OnActionLoginListener() {
            @Override
            public void onSuccess(WSResponseLogin wsResponseLogin) {
                if (wsResponseLogin != null) {
                    if (wsResponseLogin.result.status == WSResponseLogin._SUCCESS) {
                        mView.loginSuccess(wsResponseLogin);
                    } else mView.loginFailed(null);
                } else mView.loginFailed(null);
            }

            @Override
            public void onFailed(WSResponseBad wsResponseBad) {
                mView.loginFailed(wsResponseBad);
            }
        });
    }

    public void detachView() {
        mView = null;
    }
}
