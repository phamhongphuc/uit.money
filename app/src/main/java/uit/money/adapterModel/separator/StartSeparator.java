package uit.money.adapterModel.separator;

import java.util.Date;

import model.model.transaction.TransactionModel;

import static model.Const.START_SEPARATOR;

public class StartSeparator implements TransactionModel {
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
