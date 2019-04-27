package com.suryo.gamatechno.app.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.connectivity.MyConnectivity;
import com.suryo.gamatechno.app.presenter.LoginPresenter;
import com.suryo.gamatechno.app.contract.LoginContract;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseLogin;
import com.suryo.gamatechno.app.others.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageLogin extends AppCompatActivity implements LoginContract.View, View.OnClickListener {
    @BindView(R.id.edit_username)
    EditText editUsername;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);
        ButterKnife.bind(this);
        editUsername.setText("suryo.jiwandono@gmail.com");
        editPassword.setText("123456");
        loginPresenter = new LoginPresenter(this, this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
            if (isValid()) {
                if (Utility.isValidEmail(editUsername.getText().toString())) {
                    setLoginProcess(false);
                    if (MyConnectivity.isConnected(this))
                        loginPresenter.doLogin(editUsername.getText().toString(), editPassword.getText().toString());
                    else Utility.Toast(this, "No Connection");
                } else Utility.Blinked(editUsername);
            }
        }
    }

    private boolean isValid() {
        if (editUsername.getText().toString().trim().isEmpty()) {
            Utility.Blinked(editUsername);
            return false;
        } else if (editPassword.getText().toString().trim().isEmpty()) {
            Utility.Blinked(editPassword);
            return false;
        }
        return true;
    }

    private void setLoginProcess(boolean isEnd) {
        editUsername.setEnabled(isEnd);
        editPassword.setEnabled(isEnd);
        btnSubmit.setEnabled(isEnd);
    }

    @Override
    public void loginSuccess(WSResponseLogin wsResponseLogin) {
        setLoginProcess(true);
        Intent intent = new Intent(this, PageConversation.class);
        intent.putExtra("token", wsResponseLogin.result.data.token);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed(WSResponseBad wsResponseBad) {
        setLoginProcess(true);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.detachView();
        super.onDestroy();
    }
}
