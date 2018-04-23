package model.deal;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import model.Wallet;

public class Transfer extends RealmObject implements Deal {
    public Transfer() {
        time = new Date();
    }

    /**
     * <Fields>
     *
     * @see Transfer#id                     {@link Integer}
     * @see Transfer#time                   {@link Date}
     * @see Transfer#location               {@link String}
     * @see Transfer#from                   {@link Wallet}              >>  {@link Wallet#transfersFrom}
     * @see Transfer#to                     {@link Wallet}              >>  {@link Wallet#transfersTo}
     * @see Transfer#money                  {@link Long}
     */
    //region Fields [id, time, location, from, to, money]
    @PrimaryKey
    private int id;
    @Required
    private Date time;
    private String location;

    private Wallet from;
    private Wallet to;
    private long money;
    //endregion

    /**
     * <Methods>
     *
     * @see SingleLineDeal#getMoney()
     */
    //region Methods [getMoney]
    @Override
    public long getMoney() {
        return money;
    }
    //endregion
}
