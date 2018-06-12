package model;

import android.content.res.Resources;
import android.support.annotation.StringRes;

public final class Const {
    public static final int START_SEPARATOR = 0;
    public static final int DATE_SEPARATOR = 1;
    public static final int END_SEPARATOR = 2;
    public static final int BILL = 3;
    public static final int LOAN = 4;
    public static final int PAYMENT = 5;
    public static final int TRANSFER = 6;

    public static final boolean IN = true;
    public static final boolean OUT = false;

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
}