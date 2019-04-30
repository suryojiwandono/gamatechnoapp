package com.suryo.gamatechno.app.page;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.adapter.RecyclerRoomAdapter;
import com.suryo.gamatechno.app.application.MyApplication;
import com.suryo.gamatechno.app.behaviour.RefreshReceiver;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.behaviour.SyncService;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.contract.MessageContract;
import com.suryo.gamatechno.app.db.MessageHelper;
import com.suryo.gamatechno.app.db.RoomHelper;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.Message;
import com.suryo.gamatechno.app.model.Room;
import com.suryo.gamatechno.app.model.SocketRoom;
import com.suryo.gamatechno.app.model.SocketRoomChat;
import com.suryo.gamatechno.app.model.SocketRoomDetail;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.others.Constants;
import com.suryo.gamatechno.app.others.NotificationUtils;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageRoom extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_no_data)
    LinearLayout layoutNoData;
    @BindView(R.id.edit_message)
    EditText editMessage;
    @BindView(R.id.image_send)
    ImageView imageSend;

    private UserLogin userLogin;
    private RefreshReceiver refreshReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_chat);
        ButterKnife.bind(this);
        UserLoginHelper userLoginHelper = new UserLoginHelper();
        userLogin = userLoginHelper.getUserLogin();
        myService();
        imageSend.setOnClickListener(this);
    }

    private void myService() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Intent service = new Intent(this, SyncService.class);
            service.setAction(Constants.START_FG_BG_SYNC);
            startService(service);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
        isAvailable();
        if (refreshReceiver == null) refreshReceiver = new RefreshReceiver(onReceiverAction);
        IntentFilter intentFilter = new IntentFilter(Constants.REFRESH_MESSAGE);
        registerReceiver(refreshReceiver, intentFilter);
        NotificationUtils.closeNotification(this);
    }

    private void isAvailable() {
        RoomHelper roomHelper = new RoomHelper();
        List<Room> messages = roomHelper.getAll();
        if (messages != null) {
            layoutNoData.setVisibility(messages.size() > 0 ? View.GONE : View.VISIBLE);
            recyclerView.setVisibility(messages.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imageSend) {
            if (!editMessage.getText().toString().isEmpty()) {
                MyApplication myApplication = (MyApplication) getApplication();
                Socket mSocket = myApplication.getSocket();
                if (mSocket.connected()) {
                    SocketRoomChat socketRoomChat = new SocketRoomChat();
                    socketRoomChat.userId = userLogin.userId;
                    socketRoomChat.message = editMessage.getText().toString();
                    SocketRoomDetail socketRoomDetail = new SocketRoomDetail();
                    socketRoomDetail.data = socketRoomChat;
                    SocketRoom socketRoom = new SocketRoom();
                    socketRoom.request = socketRoomDetail;

                    Gson gson = new Gson();
                    mSocket.emit("send message", gson.toJson(socketRoom));
                    editMessage.setText("");
                } else {
                    Utility.Toast(this, "Socket Disconnected");
                }
            }
        }
    }

    private void onLoad() {
        RoomHelper roomHelper = new RoomHelper();
        List<Room> dataMessages = roomHelper.getAll();
        RecyclerRoomAdapter recyclerMessageAdapter = new RecyclerRoomAdapter(this, dataMessages, userLogin.userId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerMessageAdapter);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            roomHelper.setRead();
            NotificationUtils.closeNotification(this);
        });
    }

    private Response.OnReceiverAction onReceiverAction = object -> {
        onLoad();
        isAvailable();
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoomHelper roomHelper = new RoomHelper();
        roomHelper.setRead();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_room, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            if (MyConnectivity.isConnected(this)) {
                DialogMessage dialogMessage = new DialogMessage(this);
                dialogMessage.setTitle("LOGOUT");
                dialogMessage.setMessage("Do you want to Logout?");
                dialogMessage.setButtonLeft("No", Constants.ColorButton.BLACK);
                dialogMessage.setButtonRight("Yes", object -> {
                    UserLoginHelper userLoginHelper = new UserLoginHelper();
                    UserLogin userLogin = userLoginHelper.getUserLogin();
                    userLoginHelper.logout(userLogin.userId, 0);
                    finish();
                }, Constants.ColorButton.BLUE);
                dialogMessage.show();
            } else Utility.Toast(this, "No Connection");
            return true;
        } else if (id == R.id.action_conversation) {
            startActivity(new Intent(this, PageConversation.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
