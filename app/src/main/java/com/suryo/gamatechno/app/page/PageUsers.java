package com.suryo.gamatechno.app.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.RecyclerUserAdapter;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.connectivity.WSUsers;
import com.suryo.gamatechno.app.contract.UserContract;
import com.suryo.gamatechno.app.db.MUserHelper;
import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.others.Utility;
import com.suryo.gamatechno.app.presenter.UserPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageUsers extends AppCompatActivity implements UserContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_no_data)
    LinearLayout layoutNoData;
    @BindView(R.id.text_no_data)
    TextView textNoData;

    private UserPresenter userPresenter;
    private String token;
    private List<MUser> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_user);
        ButterKnife.bind(this);
        userPresenter = new UserPresenter(this, this);
        UserLoginHelper userLoginHelper = new UserLoginHelper();
        UserLogin userLogin = userLoginHelper.getUserLogin();
        if (userLogin != null) {
            token = userLogin.token;
            if (MyConnectivity.isConnected(this))
                userPresenter.doGetData(token, 1);
        }
        onLoad();
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

    private Response.OnRecyclerItemClick onRecyclerItemClick = (view, position) -> {
        if (mUsers != null) {
            Intent intent = new Intent(this, PageMessage.class);
            intent.putExtra("toUserId", mUsers.get(position).userId);
            intent.putExtra("toUsername", mUsers.get(position).username);
            intent.putExtra("fullname", mUsers.get(position).fullname);
            intent.putExtra("mUser", mUsers.get(position));
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void getDataSuccess(WSResponseDataUser wsResponseLogin) {
        isAvailable(true);
        onLoad();
    }

    private void onLoad() {
        MUserHelper mUserHelper = new MUserHelper();
        mUsers = mUserHelper.getAll();
        RecyclerUserAdapter recyclerTaskAdapter = new RecyclerUserAdapter(PageUsers.this, mUsers, onRecyclerItemClick);
        recyclerView.setAdapter(recyclerTaskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PageUsers.this));
    }

    @Override
    public void getDataFailed(WSResponseBad wsResponseBad) {
        isAvailable(false);
        textNoData.setText(wsResponseBad != null ? wsResponseBad.result.data.reqMessage : "NO DATA");
        onLoad();
    }

    @Override
    protected void onDestroy() {
        userPresenter.detachView();
        super.onDestroy();
    }
}
