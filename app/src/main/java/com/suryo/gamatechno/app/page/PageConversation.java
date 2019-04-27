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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.adapter.RecyclerConversationAdapter;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.contract.ConversationContract;
import com.suryo.gamatechno.app.db.ConversationHelper;
import com.suryo.gamatechno.app.db.MUserHelper;
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
    @BindView(R.id.text_no_data)
    TextView textNoData;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private String token = "";
    private ConversationPresenter conversationPresenter;
    private List<TConversation> tConversations;

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
            if (MyConnectivity.isConnected(this))
                conversationPresenter.doGetData("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VyX2lkIjoyLCJyb2xlIjoiVFJBVkVMTEVSIiwiY29kZSI6IjI0NzA4ZWQ5Y2UxMCIsImlhdCI6MTU1NjE4NDQxOSwiZXhwIjoxNTU3Mzk0MDE5LCJpc3MiOiJsb2NhbGhvc3QifQ.p44i2wikFOxxTkH5mNIagj47jdCtQwHY93fVkfHByuGiYVWBr4R-qBZOMiQvKH87NLpDYdwsoNMbKrbXaP9RVQ", 1);
        }
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, PageUsers.class);
            intent.putExtra("token", token);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    private void isAvailable(boolean isTrue) {
        layoutNoData.setVisibility(isTrue ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(isTrue ? View.VISIBLE : View.GONE);
    }

    @Override
    public void getDataSuccess(WSResponseDataConversation wsResponseDataConversation) {
        if (wsResponseDataConversation != null) {
            if (wsResponseDataConversation.result.status == WSResponseDataConversation._SUCCESS) {
                isAvailable(true);
                onLoad();
            } else isAvailable(false);
        }
    }


    private void onLoad() {
        ConversationHelper mUserHelper = new ConversationHelper();
        tConversations = mUserHelper.getAll();
        RecyclerConversationAdapter recyclerTaskAdapter = new RecyclerConversationAdapter(PageConversation.this, tConversations, onRecyclerItemClick);
        recyclerView.setAdapter(recyclerTaskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private Response.OnRecyclerItemClick onRecyclerItemClick = (view, position) -> {
        if (tConversations != null) {
            Intent intent = new Intent(this, PageMessage.class);
            intent.putExtra("token", token);
            intent.putExtra("toUserId", tConversations.get(position).userId);
            intent.putExtra("fullname", tConversations.get(position).fullname);
            startActivity(intent);
        }
    };

    @Override
    public void getDataFailed(WSResponseBad wsResponseBad) {
        isAvailable(false);
        textNoData.setText(wsResponseBad.result.data.reqMessage);
    }

    @Override
    protected void onDestroy() {
        conversationPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_conversation, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
