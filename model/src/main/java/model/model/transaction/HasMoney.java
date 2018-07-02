package model.model.transaction;

import io.realm.RealmModel;

public interface HasMoney extends RealmModel {
    long getMoney();
}
