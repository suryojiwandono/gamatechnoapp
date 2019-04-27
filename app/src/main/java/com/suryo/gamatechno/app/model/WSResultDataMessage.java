package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

public class WSResultDataMessage {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public WSDataMessage data;

}
