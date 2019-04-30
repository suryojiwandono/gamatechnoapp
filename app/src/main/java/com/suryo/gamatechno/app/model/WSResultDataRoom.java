package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

public class WSResultDataRoom {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public Room data;

}
