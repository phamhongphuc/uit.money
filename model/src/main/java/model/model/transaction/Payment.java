package model.model.transaction;

import com.google.common.collect.ImmutableMap;

import org.parceler.Parcel;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model_model_transaction_PaymentRealmProxy;
import model.model.Wallet;
import model.model.util.Organization;

import static model.Const.IN;
import static model.Const.PAYMENT;

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
 * @see Payment#inOrOut          {@link Boolean}
 */

@Parcel(implementations = {model_model_transaction_PaymentRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Payment.class})
public class Payment extends RealmObject implements Transaction, TransactionModel {
    public static final int PICK_LOSE = 1;
    public static final int HAVE_BREAKFAST = 2;
    public static final int HAVE_LUNCH = 3;
    public static final int HAVE_DINNER = 4;
    public static final int SALARY = 5;
    public static final int INTERNET_BILL = 6;
    public static final int ELECTRIC_BILL = 7;
    public static final int WATTER_BILL = 8;
    public static final int INITIALIZE = 9;

    private static final Map<Integer, String> KIND = ImmutableMap.<Integer, String>builder()
            .put(PICK_LOSE, "Nhặt được")
            .put(-PICK_LOSE, "Làm mất")
            .put(-HAVE_BREAKFAST, "Ăn sáng")
            .put(-HAVE_LUNCH, "Ăn trưa")
            .put(-HAVE_DINNER, "Ăn tối")

            .put(SALARY, "Nhận lương")
            .put(-INTERNET_BILL, "Hóa đơn tiền mạng")
            .put(-ELECTRIC_BILL, "Hóa đơn tiền điện")
            .put(-WATTER_BILL, "Hóa đơn tiền nước")

            .put(INITIALIZE, "Khởi tạo")

            .build();
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
    private boolean inOrOut;
    private int kind;

    public Payment() {
        time = new Date();
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = Math.abs(kind);
    }

    @Override
    public long getMoney() {
        return money * (inOrOut == IN ? 1 : -1);
    }

    public void setMoney(long money) {
        this.money = money;
    }

    @Override
    public int getType() {
        return PAYMENT;
    }

    @Override
    public String getAction() {
        String action = KIND.get(kind * (inOrOut == IN ? 1 : -1));
        if (action == null) action = "Giao dịch không xác định";
        return action;
    }

    @Override
    public boolean isInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(boolean inOrOut) {
        this.inOrOut = inOrOut;
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

    @Override
    public void initialize() {

    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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

    public void autoId() {
        RealmResults<Payment> payments = Realm.getDefaultInstance()
                .where(Payment.class)
                .findAllAsync();
        payments.load();
        id = payments.size() == 0
                ? 0
                : Objects.requireNonNull(payments.max("id")).intValue() + 1;
    }
}
