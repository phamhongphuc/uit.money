package model;

import android.content.res.Resources;
import android.support.annotation.StringRes;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Const {
    public static final int START_SEPARATOR = 0;
    public static final int DATE_SEPARATOR = 1;
    public static final int END_SEPARATOR = 2;
    public static final int BILL = 3;
    public static final int LOAN = 4;

    public static final boolean SELL = true;
    public static final boolean BUY = false;

    private static final DateFormat HOUR = new SimpleDateFormat("HH:mm", new Locale("vi"));
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("###,###,### đ");
    private static Resources resource;

    public static Resources getResource() {
        return resource;
    }

    public static void setResource(Resources resource) {
        Const.resource = resource;
    }

    public static String getString(@StringRes int id) {
        return resource.getString(id);
    }

    public static String getMoney(long money) {
        return MONEY_FORMAT.format(money);
    }

    public static String getTime(Date time) {
        return HOUR.format(time);
    }
}