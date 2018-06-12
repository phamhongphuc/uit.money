package model.model.transaction;

import io.realm.RealmModel;

public interface Transaction extends RealmModel {
    long getMoney();

    int getType();

    String getAction();

    boolean isInOrOut();
}
