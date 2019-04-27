package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserLogin extends RealmObject {
    public static final String PROPERTY_USER_ID = "user_id";
    public static final String PROPERTY_FULLNAME = "user_name";
    public static final String PROPERTY_USERNAME = "user_username";
    public static final String PROPERTY_PHONE = "user_phone";
    public static final String PROPERTY_EMAIL = "user_email";
    public static final String PROPERTY_ROLE = "role";
    public static final String PROPERTY_TOKEN = "token";
    public static final String PROPERTY_STATUS = "status";

    @PrimaryKey
    @Required
    @SerializedName(PROPERTY_USER_ID)
    public String userId;
    @SerializedName(PROPERTY_FULLNAME)
    public String fullname;
    @SerializedName(PROPERTY_USERNAME)
    public String username;
    @SerializedName(PROPERTY_PHONE)
    public String phone;
    @SerializedName(PROPERTY_EMAIL)
    public String email;
    @SerializedName(PROPERTY_ROLE)
    public String role;
    @SerializedName(PROPERTY_TOKEN)
    public String token;
    @SerializedName(PROPERTY_STATUS)
    public int status;
}
