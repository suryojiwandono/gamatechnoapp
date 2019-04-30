package com.suryo.gamatechno.app.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.adapter.RecyclerConversationAdapter;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.contract.ConversationContract;
import com.suryo.gamatechno.app.db.ConversationHelper;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.others.Constants;
import com.suryo.gamatechno.app.others.Utility;
import com.suryo.gamatechno.app.presenter.ConversationPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageConversation extends AppCompatActivity implements ConversationContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_no_data)
    LinearLayout layoutNoData;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.text_no_data)
    TextView textNoData;
    @BindView(R.id.text_size)
    TextView textSize;
    @BindView(R.id.image_left)
    ImageView imageLeft;
    @BindView(R.id.image_right)
    ImageView imageRight;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private String token = "";
    private ConversationPresenter conversationPresenter;
    private List<TConversation> tConversations;
    private int page = 1;
    private int total = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_conversation);
        ButterKnife.bind(this);
        conversationPresenter = new ConversationPresenter(this, this);
        UserLoginHelper userLoginHelper = new UserLoginHelper();
        UserLogin userLogin = userLoginHelper.getUserLogin();
        if (userLogin != null) {
            token = userLogin.token;
            getData();
        }
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, PageUsers.class);
            intent.putExtra("token", token);
            startActivity(intent);
        });
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
    }

    private void showHide() {
        imageRight.setVisibility((page == total) ? View.INVISIBLE : View.VISIBLE);
        imageLeft.setVisibility((page == 1) ? View.INVISIBLE : View.VISIBLE);
    }

    private void getData() {
        if (MyConnectivity.isConnected(this))
            conversationPresenter.doGetData(token, page);
        else onLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    private void isAvailable() {
        ConversationHelper mUserHelper = new ConversationHelper();
        List<TConversation> conversations = mUserHelper.getAll();
        if (conversations != null) {
            layoutNoData.setVisibility(conversations.size() > 0 ? View.GONE : View.VISIBLE);
            layoutData.setVisibility(conversations.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void getDataSuccess(WSResponseDataConversation wsResponseDataConversation) {
        isAvailable();
        if ((wsResponseDataConversation != null)) {
            total = wsResponseDataConversation.result.data.lastPage;
            String size = String.valueOf(page).concat("/").concat(String.valueOf(total));
            textSize.setText(size);
        }
        onLoad();
    }

    private void onLoad() {
        ConversationHelper mUserHelper = new ConversationHelper();
        tConversations = mUserHelper.getAll();
        RecyclerConversationAdapter recyclerTaskAdapter = new RecyclerConversationAdapter(this, tConversations, onRecyclerItemClick);
        recyclerView.setAdapter(recyclerTaskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        total = mUserHelper.getSize();
        String size = String.valueOf(page).concat("/").concat(String.valueOf(total));
        textSize.setText(size);
        showHide();
    }

    private Response.OnRecyclerItemClick onRecyclerItemClick = (view, position) -> {
        if (tConversations != null) {
            Intent intent = new Intent(this, PageMessage.class);
            intent.putExtra("token", token);
            intent.putExtra("toUserId", tConversations.get(position).userId);
            intent.putExtra("toUsername", tConversations.get(position).username);
            intent.putExtra("fullname", tConversations.get(position).fullname);
            startActivity(intent);
        }
    };

    @Override
    public void getDataFailed(WSResponseBad wsResponseBad) {
        Utility.Logs.v(wsResponseBad.result.data.reqMessage + "=======================>");
        isAvailable();
        textNoData.setText(wsResponseBad != null ? wsResponseBad.result.data.reqMessage : "NO DATA");
        onLoad();
    }

    @Override
    protected void onDestroy() {
        conversationPresenter.detachView();
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
