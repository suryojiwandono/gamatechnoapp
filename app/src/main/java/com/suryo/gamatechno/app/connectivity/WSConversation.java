package com.suryo.gamatechno.app.connectivity;

import android.app.Activity;

import com.google.gson.Gson;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.adapter.DialogLoader;
import com.suryo.gamatechno.app.adapter.DialogMessage;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.behaviour.async.AsyncUserDataConversation;
import com.suryo.gamatechno.app.model.WSResponseBad;
import com.suryo.gamatechno.app.model.WSResponseDataConversation;

import retrofit2.Call;

public class WSConversation {
    private Activity activity;
    private boolean isSyncSuccess;
    private Response.OnActionDataConversationListener onActionDataConversationListener;
    private DialogLoader dialogLoader;
    private ApiService apiService;
    private String token;

    public WSConversation(Activity activity, String token) {
        this.activity = activity;
        this.token = token;
        apiService = ApiClient.getClient().create(ApiService.class);

        dialogLoader = new DialogLoader(activity);
        dialogLoader.setLabel(activity.getResources().getString(R.string.label_loader_authentication));
    }

    public void setOnResponse(Response.OnActionDataConversationListener onActionDataConversationListener) {
        this.onActionDataConversationListener = onActionDataConversationListener;
    }

    public void onConversations(int page) {
        dialogLoader.show();
        Call<WSResponseDataConversation> call = apiService.conversations(token, page);
        final ApiResponseDataConversation apiResponseDataConversation = new ApiResponseDataConversation(activity);
        apiResponseDataConversation.enqueue(call, new Response.OnWSListenerDataConversation() {
            @Override
            public void onStart() {
                onActionDataConversationListener.onSuccess(null);
            }

            @Override
            public void onSuccess(WSResponseDataConversation wsResponseDataConversation) {
                onActionDataConversationListener.onSuccess(wsResponseDataConversation);
                try {
                    dialogLoader.dismiss();
                    new AsyncUserDataConversation(activity, dialogLoader, output -> dialogLoader.dismiss()).execute(wsResponseDataConversation);
                    isSyncSuccess = true;
                } catch (Exception e) {
                    dialogMessage("Users", activity.getResources().getString(R.string.dialog_message_sync_db_error), e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(WSResponseDataConversation wsResponseDataConversation) {
//                dialogMessage("Users", wsResponseData.result.message == null ? "-" : wsResponseData.result.message, null);
            }

            @Override
            public void onError(String err) {
                Gson gson = new Gson();
                WSResponseBad badRequest = gson.fromJson(err, WSResponseBad.class);
//                dialogMessage(badRequest.result.message, badRequest.result.data.reqMessage, null);
                onActionDataConversationListener.onFailed(badRequest);
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
