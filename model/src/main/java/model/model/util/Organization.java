package model.model.util;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.model_model_util_OrganizationRealmProxy;
import model.model.transaction.Loan;
import model.model.transaction.Bill;
import model.model.transaction.Payment;

@Parcel(implementations = {model_model_util_OrganizationRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Organization.class})
public class Organization extends RealmObject {
    public Organization() {

    }

    /**
     * <Fields>
     *
     * @see Organization#name                     {@link String}
     */
    //region Fields [name]
    @PrimaryKey
    private String name;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Organization#loans
     * @see Organization#bills
     * @see Organization#payments
     */
    //region LinkingObjects [loans, bills, payments, transfersFrom, transfersTo]
    @LinkingObjects("organization")
    private final RealmResults<Loan> loans = null;
    @LinkingObjects("organization")
    private final RealmResults<Bill> bills = null;
    @LinkingObjects("organization")
    private final RealmResults<Payment> payments = null;
    //endregion
}
