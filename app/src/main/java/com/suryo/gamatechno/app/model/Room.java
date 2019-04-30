package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Room extends RealmObject {

    @PrimaryKey
    @Required
    @SerializedName("message_id")
    public String messageId;
    @SerializedName("from_user_id")
    public String fromUserId;
    @SerializedName("from_user_email")
    public String fromUserEmail;
    @SerializedName("from_user_name")
    public String fromUserName;
    @SerializedName("from_user_photo")
    public String fromUserPhoto;
    @SerializedName("from_message")
    public String fromMessage;
    @SerializedName("to_user_id")
    public String toUserId;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("header_id")
    public String headerId;
    @SerializedName("is_read")
    public boolean isRead;
}
