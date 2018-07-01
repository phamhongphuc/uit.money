package model.model.transaction;

import io.realm.RealmModel;

public interface Transaction extends RealmModel, HasMoney {
    int getType();

    String getAction();

    boolean isInOrOut();
}
