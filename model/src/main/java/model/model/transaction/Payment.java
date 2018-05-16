package model.model.transaction;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model_model_transaction_PaymentRealmProxy;
import model.model.Wallet;
import model.model.util.Organization;

import static model.Const.SELL;

/**
 * Dành cho các giao dịch chỉ có một dòng
 * Ví dụ như:
 * + Thanh toán tiền điện
 * + Nhận lương
 * + Thưởng
 * + ...
 * <p>
 * <Fields>
 *
 * @see Payment#id               {@link Integer}
 * @see Payment#wallet           {@link Wallet}              >>  {@link Wallet#payments}
 * @see Payment#time             {@link Date}
 * @see Payment#money            {@link Long}
 * @see Payment#location         {@link String}
 * @see Payment#organization     {@link Organization}        >>  {@link Organization#payments}
 * @see Payment#startDate        {@link Date}
 * @see Payment#endDate          {@link Date}
 * @see Payment#buyOrSell        {@link Boolean}
 */

@Parcel(implementations = {model_model_transaction_PaymentRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Payment.class})
public class Payment extends RealmObject implements Transaction, TransactionModel {
    @PrimaryKey
    private int id;
    private Wallet wallet;
    @Required
    private Date time;
    private long money;
    private String location;
    private Organization organization;
    private Date startDate;
    private Date endDate;
    private boolean buyOrSell;

    public Payment() {
        time = new Date();
    }

    @Override
    public long getMoney() {
        return (buyOrSell == SELL ? 1 : -1) * money;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(boolean buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    @Override
    public String getAction() {
        return "Mua bán 1 dòng";
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public void initialize() {

    }
}
