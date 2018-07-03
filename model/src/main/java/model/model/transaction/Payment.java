package model.model.transaction;

import android.support.annotation.NonNull;

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
import model.model.R;
import model.model.Wallet;
import model.model.util.Organization;

import static model.Const.IN;
import static model.Const.PAYMENT;
import static model.Const.getString;
import static model.model.transaction.Payment.PaymentType.type;

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
    public static final int UNKNOWN = 0;
    public static final int PICK_LOST = 1;
    public static final int HAVE_BREAKFAST = 2;
    public static final int HAVE_LUNCH = 3;
    public static final int HAVE_DINNER = 4;
    public static final int SALARY = 5;
    public static final int INTERNET_BILL = 6;
    public static final int ELECTRIC_BILL = 7;
    public static final int WATER_BILL = 8;
    public static final int INITIALIZE = 9;

    public static final Map<Integer, PaymentType> PAYMENT_TYPE = ImmutableMap.<Integer, PaymentType>builder()
            .put(UNKNOWN, type(R.string.icon_unknown, R.string.payment_unknown))
            .put(-HAVE_BREAKFAST, type(R.string.icon_breakfast, R.string.payment_have_breakfast))
            .put(-HAVE_LUNCH, type(R.string.icon_lunch, R.string.payment_have_lunch))
            .put(-HAVE_DINNER, type(R.string.icon_dinner, R.string.payment_have_dinner))

            .put(PICK_LOST, type(R.string.icon_pick, R.string.payment_pick))
            .put(-PICK_LOST, type(R.string.icon_lost, R.string.payment_lost))
            .put(SALARY, type(R.string.icon_salary, R.string.payment_salary))

            .put(-INTERNET_BILL, type(R.string.icon_internet, R.string.payment_internet_bill))
            .put(-ELECTRIC_BILL, type(R.string.icon_electricity, R.string.payment_electric_bill))
            .put(-WATER_BILL, type(R.string.icon_water, R.string.payment_water_bill))

            .put(INITIALIZE, type(R.string.icon_create, R.string.payment_initialize))

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
        setInOrOut(kind > 0);
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
        return getPaymentType().text;
    }

    @NonNull
    public PaymentType getPaymentType() {
        final PaymentType paymentType = PAYMENT_TYPE.get(kind * (inOrOut == IN ? 1 : -1));
        if (paymentType == null) return PAYMENT_TYPE.get(UNKNOWN);
        return paymentType;
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

    public void delete(Realm realm) {
        realm.executeTransaction(r -> this.deleteFromRealm());
    }

    public static class PaymentType {
        private String text;
        private String icon;

        private PaymentType(int iconId, int textId) {
            this.icon = getString(iconId);
            this.text = getString(textId);
        }

        public static PaymentType type(int iconId, int stringId) {
            return new PaymentType(iconId, stringId);
        }

        public String getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }
    }
}
