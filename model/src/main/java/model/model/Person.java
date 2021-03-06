package model.model;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.model_model_PersonRealmProxy;
import model.model.transaction.Bill;
import model.model.transaction.Loan;

/**
 * Đại diện cho những người xung quanh người sử dụng
 * Ví dụ như: đối tác làm ăn, bạn bè, người mua / bán hàng với người sử dụng
 * <Fields>
 *
 * @see Person#name                     {@link String}
 */

@Parcel(implementations = {model_model_PersonRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Person.class})
public class Person extends RealmObject {
    @LinkingObjects("partner")
    private final RealmResults<Loan> loans = null;
    @LinkingObjects("with")
    private final RealmResults<Bill> multilineDealsWith = null;
    @PrimaryKey
    private String name;

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
