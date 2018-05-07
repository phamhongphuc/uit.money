package model.model.util;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.model_model_util_CycleRealmProxy;


@Parcel(implementations = {model_model_util_CycleRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Cycle.class})
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
