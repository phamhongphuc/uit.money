package model.model.transaction;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model_model_transaction_LoanRealmProxy;
import model.model.Person;
import model.model.R;
import model.model.Wallet;
import model.model.util.Cycle;
import model.model.util.Organization;

import static model.Const.IN;
import static model.Const.LOAN;
import static model.Const.getString;

/**
 * <Fields>
 *
 * @see Loan#id                         {@link Integer}
 * @see Loan#wallet                     {@link Wallet}              >> {@link Wallet}
 * @see Loan#money                      {@link Long}
 * @see Loan#partner                    {@link Person}              >> {@link Person#loans}
 * @see Loan#organization               {@link Organization}        >> {@link Organization#loans}
 * @see Loan#time                       {@link Date}
 * @see Loan#startDay                   {@link Date}
 * @see Loan#endDay                     {@link Date}
 * @see Loan#location                   {@link String}
 * @see Loan#interestRate               {@link Float}
 * @see Loan#cycle                      {@link Cycle}               >> Không cần linking
 * @see Loan#inOrOut               {@link Boolean}
 */
@Parcel(implementations = {model_model_transaction_LoanRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Loan.class})
public class Loan extends RealmObject implements Transaction, TransactionModel {
    @PrimaryKey
    private int id;
    private Wallet wallet;
    private long money;
    /**
     * Bắt buộc một trong 2 phải tồn tại
     * Ràng buộc này sẽ được viết trong constructor của Class
     */
    private Person partner;
    private Organization organization;
    @Required
    private Date time;
    @Required
    private Date startDay;
    private Date endDay;
    private String location;
    // Lãi suất
    private float interestRate = 0;
    /**
     * Chu kỳ vay, ví dụ như 10%/năm thì `1 năm` là chu kỳ vay
     */
    private Cycle cycle;           // TODO: ???
    /**
     * Là kiểu có mượn hay không
     * Tương ứng với IN và OUT
     */
    private boolean inOrOut;

    public Loan() {
        time = new Date();
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

    public Person getPartner() {
        return partner;
    }

    public void setPartner(Person partner) {
        this.partner = partner;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public void setInOrOut(boolean borrowOrLend) {
        this.inOrOut = borrowOrLend;
    }

    @Override
    public String getAction() {
        return getString(
                inOrOut == IN ? R.string.borrow : R.string.lend
        );
    }

    @Override
    public boolean isInOrOut() {
        return inOrOut;
    }

    @Override
    public long getMoney() {
        return (inOrOut == IN ? 1 : -1) * money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    @Override
    public int getType() {
        return LOAN;
    }

    @Override
    public void initialize() {

    }
}
