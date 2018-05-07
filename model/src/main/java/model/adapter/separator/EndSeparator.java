package model.adapter.separator;

import java.util.Date;

import model.adapter.TransactionRecyclerViewAdapter;

import static model.Const.END_SEPARATOR;

public class EndSeparator implements TransactionRecyclerViewAdapter.TransactionModel {
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
