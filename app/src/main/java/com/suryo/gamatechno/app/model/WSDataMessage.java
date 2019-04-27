package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WSDataMessage extends WSData {
    @SerializedName("message")
    public List<Message> messages;
}
