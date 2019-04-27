package com.suryo.gamatechno.app.db;

import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.TConversation;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ConversationHelper {

    private Realm mRealm;

    interface Response {
        void result(RealmResults<TConversation> data);
    }

    public ConversationHelper() {
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(configuration);
    }

    public void save(final TConversation tConversation) {
        mRealm.executeTransaction(realm -> {
            if (realm != null) {
                TConversation model = realm.copyToRealm(tConversation);
            } else {
                Utility.Logs.e("execute: Database not Exist");
            }
        });
    }


    public List<TConversation> getAll() {
        return mRealm.where(TConversation.class).findAll();
    }

    public TConversation getUserLogin() {
        return mRealm.where(TConversation.class).findFirst();
    }

    public void delete() {
        final RealmResults<TConversation> model = mRealm.where(TConversation.class).findAll();
        mRealm.executeTransaction(realm -> model.deleteAllFromRealm());
    }

    public void logout(String userId, int status) {
        mRealm.executeTransactionAsync(realm -> {
            TConversation model = realm.where(TConversation.class)
                    .equalTo("userId", userId)
                    .findFirst();

        }, () -> Utility.Logs.i("onSuccess: Update Successfully"), error -> error.printStackTrace());
    }

    public boolean isLogin() {
        RealmResults<UserLogin> results = mRealm.where(UserLogin.class).equalTo("status", 1).findAll();
        return (results.size() > 0);
    }

    public void filter(String property, int value, String propertyShort, Response response) {
        mRealm.executeTransaction(realm -> {
            RealmResults<TConversation> results = realm.where(TConversation.class).greaterThanOrEqualTo(property, value).findAllSortedAsync(propertyShort);
            response.result(results);
        });
    }
}
