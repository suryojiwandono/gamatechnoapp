package com.suryo.gamatechno.app.db;

import com.suryo.gamatechno.app.model.Room;
import com.suryo.gamatechno.app.model.UserLogin;
import com.suryo.gamatechno.app.others.Utility;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RoomHelper {

    private Realm mRealm;

    interface Response {
        void result(RealmResults<Room> data);
    }

    public RoomHelper() {
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(configuration);
    }

    public void save(final Room userLogin) {
        mRealm.executeTransaction(realm -> {
            if (realm != null) {
                Room model = realm.copyToRealmOrUpdate(userLogin);
            } else {
                Utility.Logs.e("execute: Database not Exist");
            }
        });
    }

    public void setRead() {
        mRealm.executeTransaction(realm -> {
            RealmResults<Room> model = realm.where(Room.class).findAll();
            for (Room m : model) m.isRead = true;
        });
    }

    public List<Room> getAll() {
        return mRealm.where(Room.class)
                .findAll().sort("timestamp", Sort.ASCENDING);
    }

    public List<Room> getNotification() {
        return mRealm.where(Room.class).equalTo("isRead", false)
                .findAll().sort("timestamp", Sort.ASCENDING);
    }

    public Room getRoom() {
        return mRealm.where(Room.class).findFirst();
    }

    public void delete() {
        final RealmResults<Room> model = mRealm.where(Room.class).findAll();
        mRealm.executeTransaction(realm -> model.deleteAllFromRealm());
    }

    public boolean isAlready(Room room) {
        Room results = mRealm.where(Room.class).equalTo("messageId", room.messageId).findFirst();
        return (results != null);
    }

    public void filter(String property, int value, String propertyShort, Response response) {
        mRealm.executeTransaction(realm -> {
            RealmResults<Room> results = realm.where(Room.class).greaterThanOrEqualTo(property, value).findAllSortedAsync(propertyShort);
            response.result(results);
        });
    }
}
