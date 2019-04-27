package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.others.Utility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ApiResponseDataConversation {
    private Activity activity;

    public ApiResponseDataConversation(Activity activity) {
        this.activity = activity;
    }

    public void enqueue(Call<WSResponseDataConversation> call, Response.OnWSListenerDataConversation onWSListenerDataUser) {
        Utility.Logs.i(call.request().url().toString());
        call.enqueue(new Callback<WSResponseDataConversation>() {
            @Override
            public void onResponse(Call<WSResponseDataConversation> call, retrofit2.Response<WSResponseDataConversation> response) {
                WSResponseDataConversation wsResponseDataConversation = response.body();
                onWSListenerDataUser.onStart();
                if (wsResponseDataConversation != null) {
                    if (wsResponseDataConversation.result.status == WSResponseDataConversation._SUCCESS)
                        onWSListenerDataUser.onSuccess(wsResponseDataConversation);
                    else onWSListenerDataUser.onFailed(wsResponseDataConversation);
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
            public void onFailure(Call<WSResponseDataConversation> call, Throwable t) {
                onWSListenerDataUser.onStart();
                onWSListenerDataUser.onError(t.getMessage());
                Utility.Toast(activity, t.getMessage());
                onWSListenerDataUser.onFinish();
                Utility.Logs.e(t.getMessage());
            }
        });
    }
}
