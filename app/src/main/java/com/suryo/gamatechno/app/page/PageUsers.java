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
import com.suryo.gamatechno.app.others.Constants;
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

    private UserPresenter userPresenter;
    private String token;
    private List<MUser> mUsers;
    private int page = 1;
    private int total = 1;

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
            getData();
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
    }

    private void showHide() {
        imageRight.setVisibility((page == total) ? View.INVISIBLE : View.VISIBLE);
        imageLeft.setVisibility((page == 1) ? View.INVISIBLE : View.VISIBLE);
    }

    private void getData() {
        if (MyConnectivity.isConnected(this))
            userPresenter.doGetData(token, page);
        else onLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    private void isAvailable() {
        MUserHelper mUserHelper = new MUserHelper();
        List<MUser> conversations = mUserHelper.getAll();
        if (conversations != null) {
            layoutNoData.setVisibility(conversations.size() > 0 ? View.GONE : View.VISIBLE);
            layoutData.setVisibility(conversations.size() > 0 ? View.VISIBLE : View.GONE);
        }
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
        isAvailable();
        if ((wsResponseLogin != null)) {
            total = wsResponseLogin.result.data.lastPage;
            String size = String.valueOf(page).concat("/").concat(String.valueOf(total));
            textSize.setText(size);
        }
        onLoad();
    }

    private void onLoad() {
        MUserHelper mUserHelper = new MUserHelper();
        mUsers = mUserHelper.getAll(page);
        RecyclerUserAdapter recyclerTaskAdapter = new RecyclerUserAdapter(PageUsers.this, mUsers, onRecyclerItemClick);
        recyclerView.setAdapter(recyclerTaskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PageUsers.this));
        total = mUserHelper.getSize();
        String size = String.valueOf(page).concat("/").concat(String.valueOf(total));
        textSize.setText(size);
        showHide();
    }

    @Override
    public void getDataFailed(WSResponseBad wsResponseBad) {
        isAvailable();
        textNoData.setText(wsResponseBad != null ? wsResponseBad.result.data.reqMessage : "NO DATA");
        onLoad();
    }

    @Override
    protected void onDestroy() {
        userPresenter.detachView();
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
