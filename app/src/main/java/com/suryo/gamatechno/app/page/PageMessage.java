package com.suryo.gamatechno.app.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.adapter.RecyclerMessageAdapter;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.contract.MessageContract;
import com.suryo.gamatechno.app.db.ConversationHelper;
import com.suryo.gamatechno.app.db.MUserHelper;
import com.suryo.gamatechno.app.db.MessageHelper;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.Message;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.others.Constants;
import com.suryo.gamatechno.app.others.MyDateFormat;
import com.suryo.gamatechno.app.others.Utility;
import com.suryo.gamatechno.app.presenter.MessagePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageMessage extends AppCompatActivity implements MessageContract.View, View.OnClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_no_data)
    LinearLayout layoutNoData;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.text_no_data)
    TextView textNoData;
    @BindView(R.id.edit_message)
    EditText editMessage;
    @BindView(R.id.image_send)
    ImageView imageSend;
    @BindView(R.id.text_size)
    TextView textSize;
    @BindView(R.id.image_left)
    ImageView imageLeft;
    @BindView(R.id.image_right)
    ImageView imageRight;

    private MessagePresenter messagePresenter;
    private String toUserId = "", toUsername = "";
    private UserLogin userLogin;
    private MUser mUser;
    private String token = "";
    private int page = 1;
    private int total = 1;

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
            token = userLogin.token;
            if (extra != null) {
                toUserId = extra.getString("toUserId");
                toUsername = extra.getString("toUsername");
                String fullname = extra.getString("fullname");
                mUser = extra.getParcelable("mUser");
                setTitle(fullname);
                getData();
            }
        }
        imageLeft.setOnClickListener(view -> {
            if (page > 0) {
                page--;
                getData();
                showHide();
            }
        });
        imageRight.setOnClickListener(view -> {
            if (page <= total) {
                page++;
                getData();
                showHide();
            }
        });
        imageSend.setOnClickListener(this);
    }
    private void isAvailable() {
        MessageHelper messageHelper = new MessageHelper();
        List<Message> messages = messageHelper.getAll();
        if (messages!= null) {
            layoutNoData.setVisibility(messages.size() > 0 ? View.GONE : View.VISIBLE);
            layoutData.setVisibility(messages.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }
    private void showHide() {
        imageRight.setVisibility((page == total) ? View.INVISIBLE : View.VISIBLE);
        imageLeft.setVisibility((page == 1) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    private void getData() {
        if (MyConnectivity.isConnected(this))
            messagePresenter.doGetData(token, toUserId, page);
        else onLoad();
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
        total = messageHelper.getSize();
        String size = String.valueOf(page).concat("/").concat(String.valueOf(total));
        textSize.setText(size);
        showHide();
    }

    @Override
    public void getDataSuccess(WSResponseDataMessage wsResponseDataMessage) {
        isAvailable();
        if ((wsResponseDataMessage != null)) {
            total = wsResponseDataMessage.result.data.lastPage;
            String size = String.valueOf(page).concat("/").concat(String.valueOf(total));
            textSize.setText(size);
        }
        onLoad();
    }

    @Override
    public void getDataFailed(WSResponseBad wsResponseBad) {
        isAvailable();
        onLoad();
        textNoData.setText(wsResponseBad != null ? wsResponseBad.result.data.reqMessage : "NO DATA");
    }

    @Override
    protected void onDestroy() {
        messagePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sync, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            if (MyConnectivity.isConnected(this)) {
                DialogMessage dialogMessage = new DialogMessage(this);
                dialogMessage.setTitle("SYNC");
                dialogMessage.setMessage("Do you want to get the data?");
                dialogMessage.setButtonLeft("No", Constants.ColorButton.BLACK);
                dialogMessage.setButtonRight("Yes", object -> {
                    getData();
                }, Constants.ColorButton.BLUE);
                dialogMessage.show();
            } else Utility.Toast(this, "No Connection");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
