package model;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import model.deal.Loan;
import model.deal.MultiLineDeal;

/**
 * Đại diện cho những người xung quanh người sử dụng
 * Ví dụ như: đối tác làm ăn, bạn bè, người mua / bán hàng với người sử dụng
 */

public class Person extends RealmObject {
    public Person() {

    }

    /**
     * <Fields>
     *
     * @see Person#id                       {@link Integer}
     * @see Person#name                     {@link String}
     */
    //region Fields [id, name]
    @PrimaryKey
    private int id;
    private String name;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Loan#partner
     * @see MultiLineDeal#partner
     * @see MultiLineDeal#with
     */
    //region LinkingObjects [loans, multiLineDeals, multiLineDealsWith]
    @LinkingObjects("partner")
    private final RealmResults<Loan> loans = null;
    @LinkingObjects("partner")
    private final RealmResults<MultiLineDeal> multiLineDeals = null;
    @LinkingObjects("with")
    private final RealmResults<MultiLineDeal> multiLineDealsWith = null;
    //endregion

    /**
     * <Getter> & <Setter>
     */
    //region Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion
}
