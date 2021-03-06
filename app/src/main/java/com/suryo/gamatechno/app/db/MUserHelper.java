package com.suryo.gamatechno.app.db;

import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MUserHelper {

    private Realm mRealm;

    interface Response {
        void result(RealmResults<MUser> data);
    }

    public MUserHelper() {
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(configuration);
    }

    public void save(final MUser userLogin) {
        mRealm.executeTransaction(realm -> {
            if (realm != null) {
                MUser model = realm.copyToRealmOrUpdate(userLogin);
            } else {
                Utility.Logs.e("execute: Database not Exist");
            }
        });
    }


    public List<MUser> getAll(int page) {
        return mRealm.where(MUser.class).equalTo("page", page).findAll().sort("fullname", Sort.ASCENDING);
    }

    public List<MUser> getAll() {
        return mRealm.where(MUser.class).findAll().sort("fullname", Sort.ASCENDING);
    }

    public MUser getuser() {
        return mRealm.where(MUser.class).findFirst();
    }

    public int getSize() {
        if (getAll().size() > 0)
            return mRealm.where(MUser.class).max("page").intValue();
        else return 0;
    }

    public void delete() {
        final RealmResults<MUser> model = mRealm.where(MUser.class).findAll();
        mRealm.executeTransaction(realm -> model.deleteAllFromRealm());
    }

    public void logout(String userId, int status) {
        mRealm.executeTransactionAsync(realm -> {
            MUser model = realm.where(MUser.class)
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
            RealmResults<MUser> results = realm.where(MUser.class).greaterThanOrEqualTo(property, value).findAllSortedAsync(propertyShort);
            response.result(results);
        });
    }
}
