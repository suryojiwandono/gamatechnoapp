package com.suryo.gamatechno.app.contract;

import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataUser;

public interface UserContract {
    interface View {
        void getDataSuccess(WSResponseDataUser wsResponseDataConversation);

        void getDataFailed(WSResponseBad wsResponseBad);
    }

    interface Presenter {
        void doGetData(String token, int page);
    }
}
