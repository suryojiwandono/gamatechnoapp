package com.suryo.gamatechno.app.contract;

import com.suryo.gamatechno.app.model.Room;

public interface RoomContract {
    interface View {
        void getDataSuccess(Room room);

        void getDataFailed();
    }

    interface Presenter {
        void doConnect();
    }
}
