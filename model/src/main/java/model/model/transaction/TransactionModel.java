package model.model.transaction;

import java.util.Date;

public interface TransactionModel {
    Date getTime();

    int getType();

    void initialize();
}
