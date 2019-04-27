package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class TConversation extends RealmObject {

    @SerializedName("user_id")
    public String userId;
    @SerializedName("user_key")
    public String key;
    @SerializedName("user_name")
    public String fullname;
    @SerializedName("user_username")
    public String username;
    @SerializedName("user_email")
    public String email;
    @SerializedName("user_photo")
    public String photo;
    @SerializedName("user_location")
    public String location;
    @SerializedName("user_flag")
    public String flag;
    @SerializedName("user_distance")
    public String distance;
    @SerializedName("insert_timestamp")
    public String timestamp;
    @SerializedName("is_online")
    public int isOnline;
}
