package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.model.WSResponseLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ApiResponseLogin {
    private Activity activity;

    public ApiResponseLogin(Activity activity) {
        this.activity = activity;
    }

    public void enqueue(Call<WSResponseLogin> call, Response.OnWSListenerLogin onWSListenerLogin) {
        Utility.Logs.i(call.request().url().toString());
        call.enqueue(new Callback<WSResponseLogin>() {
            @Override
            public void onResponse(Call<WSResponseLogin> call, retrofit2.Response<WSResponseLogin> response) {
                WSResponseLogin wsResponseLogin = response.body();
                onWSListenerLogin.onStart();
                if (wsResponseLogin != null) {
                    if (wsResponseLogin.result.status == WSResponseLogin._SUCCESS)
                        onWSListenerLogin.onSuccess(wsResponseLogin);
                    else onWSListenerLogin.onFailed(wsResponseLogin);
                    Utility.Logs.i("== WEB SERVICE RESPONSE ==");
                    Utility.Logs.i("STATUS : " + wsResponseLogin.result.status);
                    Utility.Logs.i("MESSAGE : " + wsResponseLogin.result.message);
                } else {
                    try {
                        onWSListenerLogin.onError(response.errorBody().string());
                        Utility.Logs.e(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                onWSListenerLogin.onFinish();
            }

            @Override
            public void onFailure(Call<WSResponseLogin> call, Throwable t) {
                onWSListenerLogin.onStart();
                onWSListenerLogin.onError(t.getMessage());
                Utility.Toast(activity, t.getMessage());
                onWSListenerLogin.onFinish();
                Utility.Logs.e(t.getMessage());
            }
        });
    }
}
