package model.model.transaction;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model_model_transaction_TransferRealmProxy;
import model.model.Wallet;

import static model.Const.IN;
import static model.Const.TRANSFER;

/**
 * <Fields>
 *
 * @see Transfer#id                     {@link Integer}
 * @see Transfer#time                   {@link Date}
 * @see Transfer#location               {@link String}
 * @see Transfer#from                   {@link Wallet}              >>  {@link Wallet}
 * @see Transfer#to                     {@link Wallet}              >>  {@link Wallet}
 * @see Transfer#money                  {@link Long}
 */
@Parcel(implementations = {model_model_transaction_TransferRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Transfer.class})
public class Transfer extends RealmObject implements Transaction, TransactionModel {
    @PrimaryKey
    private int id;
    @Required
    private Date time;
    private String location;
    private Wallet from;
    private Wallet to;
    private long money;

    public Transfer() {
        time = new Date();
    }

    @Override
    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Wallet getFrom() {
        return from;
    }

    public void setFrom(Wallet from) {
        this.from = from;
    }

    public Wallet getTo() {
        return to;
    }

    public void setTo(Wallet to) {
        this.to = to;
    }

    @Override
    public String getAction() {
        return "Chuyển tiền";
    }

    @Deprecated
    @Override
    public boolean isInOrOut() {
        return IN;
    }

    @Override
    public int getType() {
        return TRANSFER;
    }

    @Override
    public void initialize() {

    }
}
