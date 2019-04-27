package com.suryo.gamatechno.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WSData {
    @SerializedName("total")
    public int total;
    @SerializedName("per_page")
    public int perPage;
    @SerializedName("current_page")
    public String currentPage;
    @SerializedName("prev_page")
    public int prevPage;
    @SerializedName("next_page")
    public int nextPage;
    @SerializedName("last_page")
    public int lastPage;

//    @SerializedName("list_user")
//    public List<MUser> users;
//    @SerializedName("message")
//    public List<TConversationDetail> details;
}
