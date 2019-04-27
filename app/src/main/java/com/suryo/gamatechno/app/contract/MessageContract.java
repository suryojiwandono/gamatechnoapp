package com.suryo.gamatechno.app.contract;

import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.model.WSResponseDataUser;

public interface MessageContract {
    interface View {
        void getDataSuccess(WSResponseDataMessage wsResponseDataMessage);

        void getDataFailed(WSResponseBad wsResponseBad);
    }

    interface Presenter {
        void doGetData(String token, String toUserId, int page);
    }
}
