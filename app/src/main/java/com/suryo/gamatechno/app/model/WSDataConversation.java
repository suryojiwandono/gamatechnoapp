package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WSDataConversation extends WSData {
    @SerializedName("list_user")
    public List<TConversation> tConversations;
}
