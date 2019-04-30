package com.suryo.gamatechno.app.presenter;

import android.content.Context;

import com.suryo.gamatechno.app.contract.RoomContract;
import com.suryo.gamatechno.app.contract.MessageContract;

public class RoomPresenter implements RoomContract.Presenter {
    private MessageContract.View mView;
    private Context context;

    public RoomPresenter(Context context, MessageContract.View view) {
        this.context = context;
        this.mView = view;
    }

    @Override
    public void doConnect() {

    }

    public void detachView() {
        mView = null;
    }
}
