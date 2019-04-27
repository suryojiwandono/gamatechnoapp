package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.others.Utility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ApiResponseDataUser {
    private Activity activity;

    public ApiResponseDataUser(Activity activity) {
        this.activity = activity;
    }

    public void enqueue(Call<WSResponseDataUser> call, Response.OnWSListenerDataUser onWSListenerDataUser) {
        Utility.Logs.i(call.request().url().toString());
        call.enqueue(new Callback<WSResponseDataUser>() {
            @Override
            public void onResponse(Call<WSResponseDataUser> call, retrofit2.Response<WSResponseDataUser> response) {
                WSResponseDataUser wsResponseDataConversation = response.body();
                onWSListenerDataUser.onStart();
                if (wsResponseDataConversation != null) {
                    if (wsResponseDataConversation.result.status == WSResponseDataUser._SUCCESS)
                        onWSListenerDataUser.onSuccess(wsResponseDataConversation);
                    else onWSListenerDataUser.onFailed(wsResponseDataConversation);
                    Utility.Logs.i("== WEB SERVICE RESPONSE ==");
                    Utility.Logs.i("STATUS : " + wsResponseDataConversation.result.status);
                    Utility.Logs.i("MESSAGE : " + wsResponseDataConversation.result.message);
                } else {
                    try {
                        onWSListenerDataUser.onError(response.errorBody().string());
                        Utility.Logs.e(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                onWSListenerDataUser.onFinish();
            }

            @Override
            public void onFailure(Call<WSResponseDataUser> call, Throwable t) {
                onWSListenerDataUser.onStart();
                onWSListenerDataUser.onError(t.getMessage());
                Utility.Toast(activity, t.getMessage());
                onWSListenerDataUser.onFinish();
                Utility.Logs.e(t.getMessage());
            }
        });
    }
}
