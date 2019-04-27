package com.suryo.gamatechno.app.db;

import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class UserLoginHelper {

    private Realm mRealm;

    interface Response {
        void result(RealmResults<UserLogin> data);
    }

    public UserLoginHelper() {
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(configuration);
    }

    public void save(final UserLogin userLogin) {
        mRealm.executeTransaction(realm -> {
            if (realm != null) {
                UserLogin model = realm.copyToRealmOrUpdate(userLogin);
            } else {
                Utility.Logs.e("execute: Database not Exist");
            }
        });
    }


    public List<UserLogin> getAll() {
        return mRealm.where(UserLogin.class).findAll();
    }

    public UserLogin getUserLogin() {
        return mRealm.where(UserLogin.class).findFirst();
    }

    public void delete(Integer id) {
        final RealmResults<UserLogin> model = mRealm.where(UserLogin.class).equalTo("id", id).findAll();
        mRealm.executeTransaction(realm -> model.deleteFromRealm(0));
    }

    public void logout(String userId, int status) {
        mRealm.executeTransactionAsync(realm -> {
            UserLogin model = realm.where(UserLogin.class)
                    .equalTo("userId", userId)
                    .findFirst();
            model.status = status;
        }, () -> Utility.Logs.i("onSuccess: Update Successfully"), error -> error.printStackTrace());
    }

    public boolean isLogin() {
        RealmResults<UserLogin> results = mRealm.where(UserLogin.class).equalTo("status", 1).findAll();
        return (results.size() > 0);
    }

    public void filter(String property, int value, String propertyShort, Response response) {
        mRealm.executeTransaction(realm -> {
            RealmResults<UserLogin> results = realm.where(UserLogin.class).greaterThanOrEqualTo(property, value).findAllSortedAsync(propertyShort);
            response.result(results);
        });
    }
}
