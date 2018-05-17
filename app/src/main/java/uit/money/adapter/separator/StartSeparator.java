package uit.money.adapter.separator;

import java.util.Date;

import model.model.transaction.TransactionModel;

import static model.Const.START_SEPARATOR;

public class StartSeparator implements TransactionModel {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Date getTime() {
        return null;
    }

    @Override
    public int getType() {
        return START_SEPARATOR;
    }

    @Override
    public void initialize() {

    }
}
