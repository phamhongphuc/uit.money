package model.deal;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import model.Person;
import model.Wallet;
import model.util.Item;
import model.util.Organization;

/**
 * MultiLineDeal tức là hóa đơn
 * ở đây, MultiLineDeal đại diện cho một danh sách các Item
 * ví dụ: đây là hóa đơn đi mua sắm gồm có
 * + 2 chai nước
 * + 3 cái ly
 */
public class MultiLineDeal extends RealmObject implements Deal {
    public MultiLineDeal() {
        time = new Date();
    }

    /**
     * <Constants>
     *
     * @see SingleLineDeal#SELL
     * @see SingleLineDeal#BUY
     */
    //region Constants [SELL, BUY]
    public static final boolean SELL = true;    // +
    public static final boolean BUY = false;    // -
    //endregion

    /**
     * <Fields>
     *
     * @see MultiLineDeal#id                {@link Integer}
     * @see MultiLineDeal#wallet            {@link Wallet}              >>  {@link Wallet#multiLineDeals}
     * @see MultiLineDeal#time              {@link Date}
     * @see MultiLineDeal#location          {@link String}
     * @see MultiLineDeal#partner           {@link Person}              >>  {@link Person#multiLineDeals}
     * @see MultiLineDeal#with              {@link Person}              >>  {@link Person#multiLineDealsWith}
     * @see MultiLineDeal#organization      {@link Organization}        >>  {@link Organization#multiLineDeals}
     * @see MultiLineDeal#buyOrSell         {@link Boolean}
     */
    //region Fields [id, wallet, time, location, partner, with, organization, buyOrSell]
    @PrimaryKey
    private int id;
    private Wallet wallet;
    @Required
    private Date time;
    private String location;

    private Person partner;
    private Person with;

    private Organization organization;

    private boolean buyOrSell;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Wallet#user
     */
    //region LinkingObjects [items]
    @LinkingObjects("multiLineDeal")
    private final RealmResults<Item> items = null;
    //endregion

    /**
     * <Methods>
     *
     * @see MultiLineDeal#getMoney()
     */
    //region Methods [getMoney]
    @Override
    public long getMoney() {
        long money = 0;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Item item : items) {
            money += item.getMoney();
        }
        realm.commitTransaction();
        return (buyOrSell == SELL ? 1 : -1) * money;
    }
    //endregion
}