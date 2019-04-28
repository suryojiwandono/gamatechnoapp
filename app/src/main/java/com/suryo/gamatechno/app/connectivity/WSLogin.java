package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.google.gson.Gson;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.behaviour.async.AsyncUserLogin;
import com.suryo.gamatechno.app.model.WSResponseLogin;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.others.Constants;

import retrofit2.Call;

public class WSLogin {
    private Activity activity;
    private boolean isSyncSuccess;
    private Response.OnActionLoginListener onActionLoginListener;
    private DialogLoader dialogLoader;
    private ApiService apiService;

    public WSLogin(Activity activity) {
        this.activity = activity;
        apiService = ApiClient.getClient().create(ApiService.class);

        dialogLoader = new DialogLoader(activity);
        dialogLoader.setLabel(activity.getResources().getString(R.string.label_loader_authentication));
    }

    public void setOnResponse(Response.OnActionLoginListener onActionLoginListener) {
        this.onActionLoginListener = onActionLoginListener;
    }

    public void onLogin(final String username, String password) {
        dialogLoader.show();
        Call<WSResponseLogin> call = apiService.login(username.concat(Constants.prefixEmail), password);
        final ApiResponseLogin apiResponseLogin = new ApiResponseLogin(activity);
        apiResponseLogin.enqueue(call, new Response.OnWSListenerLogin() {
            @Override
            public void onStart() {
                onActionLoginListener.onSuccess(null);
            }

            @Override
            public void onSuccess(WSResponseLogin wsResponseLogin) {
                try {
                    new AsyncUserLogin(activity, dialogLoader, output -> {
                        onActionLoginListener.onSuccess(wsResponseLogin);
                        dialogLoader.dismiss();
                    }).execute(wsResponseLogin);
                    isSyncSuccess = true;
                } catch (Exception e) {
                    dialogMessage("Login", activity.getResources().getString(R.string.dialog_message_sync_db_error), e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(WSResponseLogin wsResponseLogin) {
                onActionLoginListener.onSuccess(wsResponseLogin);
                dialogMessage("Login", wsResponseLogin.result.message == null ? "-" : wsResponseLogin.result.message, null);
            }

            @Override
            public void onError(String err) {
                Gson gson = new Gson();
                WSResponseBad badRequest = gson.fromJson(err, WSResponseBad.class);
                dialogMessage(badRequest.result.message, badRequest.result.data.reqMessage, null);
                onActionLoginListener.onFailed(badRequest);
            }


            @Override
            public void onFinish() {
                if (!isSyncSuccess) dialogLoader.dismiss();
            }
        });
    }

    private void dialogMessage(String title, String message, String messageDetail) {
        DialogMessage dialogMessage = new DialogMessage(activity);
        dialogMessage.setTitle(title);
        dialogMessage.setMessage(message);
        dialogMessage.setMessageDetail(messageDetail);
        dialogMessage.setButtonMiddle(activity.getResources().getString(R.string.button_ok));
        dialogMessage.show();
    }
}
