package uit.money.adapterModel.separator;

import java.util.Date;

import model.model.transaction.TransactionModel;

import static model.Const.END_SEPARATOR;

public class EndSeparator implements TransactionModel {
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
