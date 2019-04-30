package com.suryo.gamatechno.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MUser extends RealmObject implements Parcelable {

    @PrimaryKey
    @Required
    @SerializedName("user_id")
    public String userId;
    @SerializedName("user_key")
    public String key;
    @SerializedName("user_name")
    public String fullname;
    @SerializedName("user_username")
    public String username;
    @SerializedName("user_email")
    public String email;
    @SerializedName("user_photo")
    public String photo;
    @SerializedName("user_location")
    public String location;
    @SerializedName("user_flag")
    public String flag;
    @SerializedName("user_distance")
    public String distance;
    @SerializedName("user_online")
    public int isOnline;
    @SerializedName("page")
    public int page;

    public MUser() {
    }

    private MUser(Parcel in) {
        userId = in.readString();
        key = in.readString();
        fullname = in.readString();
        username = in.readString();
        email = in.readString();
        photo = in.readString();
        location = in.readString();
        flag = in.readString();
        distance = in.readString();
        isOnline = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(key);
        dest.writeString(fullname);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(photo);
        dest.writeString(location);
        dest.writeString(flag);
        dest.writeString(distance);
        dest.writeInt(isOnline);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MUser> CREATOR = new Parcelable.Creator<MUser>() {
        @Override
        public MUser createFromParcel(Parcel in) {
            return new MUser(in);
        }

        @Override
        public MUser[] newArray(int size) {
            return new MUser[size];
        }
    };

}
