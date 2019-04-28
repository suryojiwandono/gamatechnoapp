package com.suryo.gamatechno.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.suryo.gamatechno.app.db.UserLoginHelper;
import com.suryo.gamatechno.app.page.PageConversation;
import com.suryo.gamatechno.app.page.PageLogin;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main extends AppCompatActivity {
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        UserLoginHelper userLoginHelper = new UserLoginHelper();
        boolean isLogin = userLoginHelper.isLogin();
        startActivity(new Intent(this, isLogin ? PageConversation.class : PageLogin.class));
        finish();
    }
}
