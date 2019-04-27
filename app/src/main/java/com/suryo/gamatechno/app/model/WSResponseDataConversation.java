package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

public class WSResponseDataConversation {
    @SerializedName("gtfwResult")
    public WSResultDataConversation result;

    public static final int _ERROR = -1;
    public static final int _FAILED = 403;
    public static final int _SUCCESS = 200;
    public static final int _NULL = 2;
    public static final int _CON_FAILED = 3;
}
