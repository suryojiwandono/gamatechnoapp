package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.google.gson.Gson;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.behaviour.async.AsyncUserData;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataUser;

import retrofit2.Call;

public class WSUsers {
    private Activity activity;
    private boolean isSyncSuccess;
    private Response.OnActionDataUserListener onActionDataUserListener;
    private DialogLoader dialogLoader;
    private ApiService apiService;
    private String token;

    public WSUsers(Activity activity, String token) {
        this.activity = activity;
        this.token = token;
        apiService = ApiClient.getClient().create(ApiService.class);

        dialogLoader = new DialogLoader(activity);
        dialogLoader.setLabel(activity.getResources().getString(R.string.label_loader_authentication));
    }

    public void setOnResponse(Response.OnActionDataUserListener onActionDataConversationListener) {
        this.onActionDataUserListener = onActionDataConversationListener;
    }

    public void onUsers() {
        dialogLoader.show();
        Call<WSResponseDataUser> call = apiService.users(token, "1");
        final ApiResponseDataUser apiResponseDataConversation = new ApiResponseDataUser(activity);
        apiResponseDataConversation.enqueue(call, new Response.OnWSListenerDataUser() {
            @Override
            public void onStart() {
                onActionDataUserListener.onSuccess(null);
            }

            @Override
            public void onSuccess(WSResponseDataUser wsResponseDataUser) {
                try {
                    new AsyncUserData(activity, dialogLoader, output -> {
                        onActionDataUserListener.onSuccess(wsResponseDataUser);
                        dialogLoader.dismiss();
                    }).execute(wsResponseDataUser);
                    isSyncSuccess = true;
                } catch (Exception e) {
                    dialogMessage("Users", activity.getResources().getString(R.string.dialog_message_sync_db_error), e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(WSResponseDataUser wsResponseDataConversation) {
                onActionDataUserListener.onSuccess(wsResponseDataConversation);
                dialogMessage("Users", wsResponseDataConversation.result.message == null ? "-" : wsResponseDataConversation.result.message, null);
            }

            @Override
            public void onError(String err) {
                Gson gson = new Gson();
                WSResponseBad badRequest = gson.fromJson(err, WSResponseBad.class);
                dialogMessage(badRequest.result.message, badRequest.result.data.reqMessage, null);
                onActionDataUserListener.onFailed(badRequest);
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
