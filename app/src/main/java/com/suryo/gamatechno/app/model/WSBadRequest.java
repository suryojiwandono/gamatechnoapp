package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

public class WSBadRequest {
    @SerializedName("gtfwResult")
    public String reqStatus;
    @SerializedName("reqMessage")
    public String reqMessage;
}
