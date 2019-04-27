package com.suryo.gamatechno.app.contract;

import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;

public interface ConversationContract {
    interface View {
        void getDataSuccess(WSResponseDataConversation wsResponseDataConversation);

        void getDataFailed(WSResponseBad wsResponseBad);
    }

    interface Presenter {
        void doGetData(String token, int page);
    }
}
