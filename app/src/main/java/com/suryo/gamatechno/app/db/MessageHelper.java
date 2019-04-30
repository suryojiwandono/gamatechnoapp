package com.suryo.gamatechno.app.db;

import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.Message;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MessageHelper {

    private Realm mRealm;

    interface Response {
        void result(RealmResults<Message> data);
    }

    public int getSize() {
        if (getAll().size() > 0)
            return mRealm.where(Message.class).max("page").intValue();
        else return 0;
    }

    public MessageHelper() {
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(configuration);
    }

    public void save(final Message userLogin) {
        mRealm.executeTransaction(realm -> {
            if (realm != null) {
                Message model = realm.copyToRealmOrUpdate(userLogin);
            } else {
                Utility.Logs.e("execute: Database not Exist");
            }
        });
    }


    public List<Message> getAll(String toUserId) {
        return mRealm.where(Message.class)
                .equalTo("toUserId", toUserId)
                .findAll().sort("timestamp", Sort.ASCENDING);
    }

    public List<Message> getAll() {
        return mRealm.where(Message.class)
                .findAll().sort("timestamp", Sort.ASCENDING);
    }

    public Message getUserLogin() {
        return mRealm.where(Message.class).findFirst();
    }

    public void delete() {
        final RealmResults<Message> model = mRealm.where(Message.class).findAll();
        mRealm.executeTransaction(realm -> model.deleteAllFromRealm());
    }

    public boolean isLogin() {
        RealmResults<UserLogin> results = mRealm.where(UserLogin.class).equalTo("status", 1).findAll();
        return (results.size() > 0);
    }

    public void filter(String property, int value, String propertyShort, Response response) {
        mRealm.executeTransaction(realm -> {
            RealmResults<Message> results = realm.where(Message.class).greaterThanOrEqualTo(property, value).findAllSortedAsync(propertyShort);
            response.result(results);
        });
    }
}
