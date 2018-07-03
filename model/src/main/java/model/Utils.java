package model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.model.R;
import model.model.transaction.Transaction;

import static model.Const.getResource;

public class Utils {
    private static final DateFormat HOUR = new SimpleDateFormat("HH:mm", new Locale("vi"));
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm' - 'dd/MM/yyyy", new Locale("vi"));
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("###,###,### đ");

    public static String getMoney(long money) {
        return MONEY_FORMAT.format(money);
    }

    public static int getMoneyColor(Transaction transaction) {
        return getMoneyColor(transaction.isInOrOut());
    }

    public static int getMoneyColor(Boolean inOrOut) {
        return getResource().getColor(inOrOut ? R.color.in_color : R.color.out_color, null
        );
    }

    public static String getTime(Date time) {
        return HOUR.format(time);
    }

    public static String getDateTime(Date time) {
        return DATE_FORMAT.format(time);
    }
}
