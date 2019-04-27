package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Message extends RealmObject {
    @SerializedName("message_id")
    public String messageId;
    @SerializedName("from_user_id")
    public String fromUserId;
    @SerializedName("to_user_id")
    public String toUserId;
    @SerializedName("from_user_name")
    public String fromUsername;
    @SerializedName("to_user_name")
    public String toUsername;
    @SerializedName("message_content")
    public String messageContent;
    @SerializedName("status")
    public String status;
    @SerializedName("message_status")
    public String messageStatus;
    @SerializedName("timestamp")
    public String timestamp;
}
