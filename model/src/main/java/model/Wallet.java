package model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import model.deal.Loan;
import model.deal.MultiLineDeal;
import model.deal.SingleLineDeal;
import model.deal.Transfer;

public class Wallet extends RealmObject {
    public Wallet() {

    }

    /**
     * <Fields>
     *
     * @see Wallet#id                       {@link Integer}
     * @see Wallet#name                     {@link String}
     * @see Wallet#user                     {@link User}                >>  {@link User#wallets}
     * @see Wallet#total                    {@link Long}
     */
    //region Fields [id, name, user, total]
    @PrimaryKey
    private int id;
    @Required
    private String name;
    private User user;
    private long total;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Loan#wallet             loans
     * @see MultiLineDeal#wallet    multiLineDeals
     * @see SingleLineDeal#wallet   singleLineDeals
     * @see Transfer#from           transfersFrom
     * @see Transfer#to             transfersTo
     */
    //region LinkingObjects [loans, multiLineDeals, singleLineDeals, transfersFrom, transfersTo]
    @LinkingObjects("wallet")
    private final RealmResults<Loan> loans = null;
    @LinkingObjects("wallet")
    private final RealmResults<MultiLineDeal> multiLineDeals = null;
    @LinkingObjects("wallet")
    private final RealmResults<SingleLineDeal> singleLineDeals = null;
    @LinkingObjects("from")
    private final RealmResults<Transfer> transfersFrom = null;
    @LinkingObjects("to")
    private final RealmResults<Transfer> transfersTo = null;
    //endregion

    /**
     * <Methods>
     *
     * @see Wallet#calcTotal()
     */
    //region Methods [calcTotal]
    private void calcTotal() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        total = 0;
        for (Loan loan : loans) {
            total += loan.getMoney();
        }
        for (SingleLineDeal singleLineDeal : singleLineDeals) {
            total += singleLineDeal.getMoney();
        }
        for (MultiLineDeal multiLineDeal : multiLineDeals) {
            total += multiLineDeal.getMoney();
        }
        for (Transfer transferFrom : transfersFrom) {
            total += transferFrom.getMoney();
        }
        for (Transfer transferTo : transfersTo) {
            total += transferTo.getMoney();
        }
        realm.commitTransaction();
    }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
    //endregion
}