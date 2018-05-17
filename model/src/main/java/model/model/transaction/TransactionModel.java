package model.model.transaction;

import java.util.Date;

public interface TransactionModel {
    int getId();

    Date getTime();

    int getType();

    void initialize();
}
