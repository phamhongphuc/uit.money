package model.deal.loan;

import io.realm.RealmObject;

public class Cycle extends RealmObject {
    public Cycle() {

    }

    //region Constants [...]
    private static int DAY = 0;
    private static int MONTH = 1;
    private static int YEAR = 2;
    //endregion

    //region Fields [...]
    private int type;
    //endregion
}
