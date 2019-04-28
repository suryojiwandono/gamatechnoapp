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
import com.suryo.gamatechno.app.db.ConversationHelper;
import com.suryo.gamatechno.app.db.MessageHelper;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.Message;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.others.MyDateFormat;
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
    private String toUserId = "", toUsername = "";
    private UserLogin userLogin;
    private MUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_message);
        ButterKnife.bind(this);
        Bundle extra = getIntent().getExtras();
        messagePresenter = new MessagePresenter(this, this);
        UserLoginHelper userLoginHelper = new UserLoginHelper();
        userLogin = userLoginHelper.getUserLogin();
        if (userLogin != null) {
            String token = userLogin.token;
            if (extra != null) {
                toUserId = extra.getString("toUserId");
                toUsername = extra.getString("toUsername");
                String fullname = extra.getString("fullname");
                mUser = extra.getParcelable("mUser");
                setTitle(fullname);
                if (MyConnectivity.isConnected(this))
                    messagePresenter.doGetData(token, toUserId, 1);
            }
        }
        onLoad();
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
            if (!editMessage.getText().toString().isEmpty()) {
                MessageHelper mUserHelper = new MessageHelper();
                Message message = new Message();
                message.messageId = MyDateFormat.format(MyDateFormat.Pattern.databaseID);
                message.fromUserId = userLogin.userId;
                message.toUserId = toUserId;
                message.fromUsername = userLogin.username;
                message.toUsername = toUsername;
                message.messageContent = editMessage.getText().toString();
                message.status = "SENDER";
                message.messageStatus = "PENDING";
                message.timestamp = MyDateFormat.format(MyDateFormat.Pattern.databaseDateTime);
                mUserHelper.save(message);

                ConversationHelper conversationHelper = new ConversationHelper();
                if (conversationHelper.getUser(toUserId) == null && mUser != null) {
                    TConversation tConversation = new TConversation();
                    tConversation.userId = toUserId;
                    tConversation.key = mUser.key;
                    tConversation.fullname = mUser.fullname;
                    tConversation.username = toUsername;
                    tConversation.email = mUser.email;
                    tConversation.photo = mUser.photo;
                    tConversation.location = mUser.location;
                    tConversation.flag = mUser.flag;
                    tConversation.distance = mUser.distance;
                    tConversation.timestamp = MyDateFormat.format(MyDateFormat.Pattern.databaseDateTime);
                    tConversation.isOnline = 1;
                    conversationHelper.save(tConversation);
                }

                onLoad();
                editMessage.setText("");
            }
        }
    }

    private void onLoad() {
        MessageHelper messageHelper = new MessageHelper();
        List<Message> dataMessages = messageHelper.getAll(toUserId);
        RecyclerMessageAdapter recyclerMessageAdapter = new RecyclerMessageAdapter(this, dataMessages, userLogin.userId);
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
        onLoad();
        Utility.Toast(this, wsResponseBad != null ? wsResponseBad.result.data.reqMessage : "NO DATA");
    }

    @Override
    protected void onDestroy() {
        messagePresenter.detachView();
        super.onDestroy();
    }
}
