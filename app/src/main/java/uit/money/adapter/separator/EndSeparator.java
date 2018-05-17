package uit.money.adapter.separator;

import java.util.Date;

import model.model.transaction.TransactionModel;

import static model.Const.END_SEPARATOR;

public class EndSeparator implements TransactionModel {
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
        return END_SEPARATOR;
    }

    @Override
    public void initialize() {

    }
}
