package com.suryo.gamatechno.app.connectivity;

import com.suryo.gamatechno.app.model.WSResponseDataConversation;
import com.suryo.gamatechno.app.model.WSResponseDataMessage;
import com.suryo.gamatechno.app.model.WSResponseDataUser;
import com.suryo.gamatechno.app.model.WSResponseLogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    Call<WSResponseLogin> login(@Field("username") String username, @Field("password") String password);

    @GET("api/list_conversation")
    Call<WSResponseDataConversation> conversations(@Header("token") String token, @Query("page") int page);

    @GET("api/list_user")
    Call<WSResponseDataUser> users(@Header("token") String token, @Query("page") String page);

    @GET("api/get_history_message")
    Call<WSResponseDataMessage> historyMessages(@Header("token") String token, @Query("to_user_id") String toUserId, @Query("page") int page);

}
