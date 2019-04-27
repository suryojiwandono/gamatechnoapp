package com.suryo.gamatechno.app.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.RecyclerMessageAdapter;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.contract.MessageContract;
import com.suryo.gamatechno.app.db.MessageHelper;
import com.suryo.gamatechno.app.model.Message;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.others.Utility;
import com.suryo.gamatechno.app.presenter.MessagePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageMessage extends AppCompatActivity implements MessageContract.View, View.OnClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.edit_message)
    EditText editMessage;
    @BindView(R.id.image_send)
    ImageView imageSend;

    private MessagePresenter messagePresenter;
    private String toUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_message);
        ButterKnife.bind(this);
        Bundle extra = getIntent().getExtras();
        messagePresenter = new MessagePresenter(this, this);
        if (extra != null) {
            String token = extra.getString("token");
            toUserId = extra.getString("toUserId");
            String fullname = extra.getString("fullname");
            setTitle(fullname);
            if (MyConnectivity.isConnected(this))
                messagePresenter.doGetData("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VyX2lkIjoyLCJyb2xlIjoiVFJBVkVMTEVSIiwiY29kZSI6IjI0NzA4ZWQ5Y2UxMCIsImlhdCI6MTU1NjE4NDQxOSwiZXhwIjoxNTU3Mzk0MDE5LCJpc3MiOiJsb2NhbGhvc3QifQ.p44i2wikFOxxTkH5mNIagj47jdCtQwHY93fVkfHByuGiYVWBr4R-qBZOMiQvKH87NLpDYdwsoNMbKrbXaP9RVQ", toUserId, 1);
        }
        imageSend.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    @Override
    public void onClick(View view) {
        if (view == imageSend) {

        }
    }

    private void onLoad() {
        MessageHelper messageHelper = new MessageHelper();
        List<Message> messages = messageHelper.getAll();
        RecyclerMessageAdapter recyclerMessageAdapter = new RecyclerMessageAdapter(this, messages, toUserId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerMessageAdapter);
    }

    @Override
    public void getDataSuccess(WSResponseDataMessage wsResponseDataMessage) {
        onLoad();
    }

    @Override
    public void getDataFailed(WSResponseBad wsResponseBad) {
        Utility.Toast(this, wsResponseBad.result.data.reqMessage);
    }

    @Override
    protected void onDestroy() {
        messagePresenter.detachView();
        super.onDestroy();
    }
}
