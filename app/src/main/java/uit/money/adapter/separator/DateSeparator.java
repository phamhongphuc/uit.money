package uit.money.adapter.separator;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import model.model.R;
import model.model.transaction.TransactionModel;

import static model.Const.DATE_SEPARATOR;
import static model.Const.getString;

public class DateSeparator implements TransactionModel {
    private Date time;

    public DateSeparator(Date time) {
        this.time = time;
    }

    private static int getDays(Date time) {
        final int a = (int) TimeUnit.DAYS.convert(time.getTime(), TimeUnit.MILLISECONDS);
        final int b = (int) TimeUnit.DAYS.convert(new Date().getTime(), TimeUnit.MILLISECONDS);
        return a - b;
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public int getType() {
        return DATE_SEPARATOR;
    }

    @Override
    public void initialize() {

    }

    public String getTimeString() {
        final int days = getDays(time);
        switch (days) {
            case 0:
                return getString(R.string.today);
            case -1:
                return getString(R.string.yesterday);
            default:
                return -days + " " + getString(R.string.day_ago);
        }
    }
}
