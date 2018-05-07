package model.adapter.separator;

import java.util.Date;

import model.adapter.TransactionRecyclerViewAdapter;

import static model.Const.START_SEPARATOR;

public class StartSeparator implements TransactionRecyclerViewAdapter.TransactionModel {
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
