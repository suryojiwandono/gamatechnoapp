package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.behaviour.async.AsyncUserDataMessage;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.model.WSResponseDataUser;

import retrofit2.Call;

public class WSMessage {
    private Activity activity;
    private boolean isSyncSuccess;
    private Response.OnActionDataMessageListener onActionDataMessageListener;
    private DialogLoader dialogLoader;
    private ApiService apiService;
    private String token;

    public WSMessage(Activity activity, String token) {
        this.activity = activity;
        this.token = token;
        apiService = ApiClient.getClient().create(ApiService.class);

        dialogLoader = new DialogLoader(activity);
        dialogLoader.setLabel(activity.getResources().getString(R.string.label_loader_authentication));
    }

    public void setOnResponse(Response.OnActionDataMessageListener onActionDataConversationListener) {
        this.onActionDataMessageListener = onActionDataConversationListener;
    }

    public void onMessage(String token, String toUserId, int page) {
        dialogLoader.show();
        Call<WSResponseDataMessage> call = apiService.historyMessages(token, toUserId, page);
        final ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(activity);
        apiResponseDataMessage.enqueue(call, new Response.OnWSListenerDataMessage() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(WSResponseDataMessage wsResponseDataMessage) {
                try {
                    dialogLoader.dismiss();
                    new AsyncUserDataMessage(activity, dialogLoader, output -> {
                        onActionDataMessageListener.onSuccess(wsResponseDataMessage);
                        dialogLoader.dismiss();
                    }).execute(wsResponseDataMessage);
                    isSyncSuccess = true;
                } catch (Exception e) {
                    dialogMessage("Users", activity.getResources().getString(R.string.dialog_message_sync_db_error), e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(WSResponseDataMessage wsResponseDataConversation) {
                onActionDataMessageListener.onSuccess(wsResponseDataConversation);
                dialogMessage("Users", wsResponseDataConversation.result.message == null ? "-" : wsResponseDataConversation.result.message, null);
            }

            @Override
            public void onError(String err) {
                Gson gson = new Gson();
                try {
                    WSResponseBad badRequest = gson.fromJson(err, WSResponseBad.class);
//                dialogMessage(badRequest.result.message, badRequest.result.data.reqMessage, null);
                    onActionDataMessageListener.onFailed(badRequest);
                } catch (JsonSyntaxException e) {
                    onActionDataMessageListener.onFailed(null);
                }
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
