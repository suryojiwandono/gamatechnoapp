package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WSDataUser extends WSData {
    @SerializedName("list_user")
    public List<MUser> users;
}
