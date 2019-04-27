package com.suryo.gamatechno.app.contract;

import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseLogin;

public interface LoginContract {
    interface View {
        void loginSuccess(WSResponseLogin wsResponseLogin);

        void loginFailed(WSResponseBad wsResponseBad);
    }

    interface Presenter {
        void doLogin(String username, String password);
    }
}
