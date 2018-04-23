package model.util;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import model.deal.Loan;
import model.deal.MultiLineDeal;
import model.deal.SingleLineDeal;

public class Organization extends RealmObject {
    public Organization() {

    }

    /**
     * <Fields>
     *
     * @see String#name                     {@link String}
     */
    //region Fields [name]
    @PrimaryKey
    private String name;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Organization#loans
     * @see Organization#multiLineDeals
     * @see Organization#singleLineDeals
     */
    //region LinkingObjects [loans, multiLineDeals, singleLineDeals, transfersFrom, transfersTo]
    @LinkingObjects("organization")
    private final RealmResults<Loan> loans = null;
    @LinkingObjects("organization")
    private final RealmResults<MultiLineDeal> multiLineDeals = null;
    @LinkingObjects("organization")
    private final RealmResults<SingleLineDeal> singleLineDeals = null;
    //endregion
}
