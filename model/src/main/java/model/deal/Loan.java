package model.deal;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import model.Person;
import model.Wallet;
import model.deal.loan.Cycle;
import model.util.Organization;

public class Loan extends RealmObject implements Deal {
    public Loan() {
        time = new Date();
    }

    /**
     * <Constants>
     *
     * @see Loan#BORROW
     * @see Loan#LEND
     */
    //region Constants [BORROW, LEND]
    private static boolean BORROW = true;   // Mượn của ai
    private static boolean LEND = false;    // Cho ai mượn
    //endregion

    /**
     * <Fields>
     *
     * @see Loan#id                         {@link Integer}
     * @see Loan#wallet                     {@link Wallet}              >> {@link Wallet#loans}
     * @see Loan#money                      {@link Long}
     * @see Loan#partner                    {@link Person}              >> {@link Person#loans}
     * @see Loan#organization               {@link Organization}        >> {@link Organization#loans}
     * @see Loan#time                       {@link Date}
     * @see Loan#startDay                   {@link Date}
     * @see Loan#endDay                     {@link Date}
     * @see Loan#location                   {@link String}
     * @see Loan#interestRate               {@link Float}
     * @see Loan#cycle                      {@link Cycle}               >> Không cần linking
     * @see Loan#borrowOrLend               {@link Boolean}
     */
    //region Fields [id, wallet, partner, organization, startDay, endDay, location, cycle, borrowOrLend]
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
     * Tương ứng với BORROW và LEND
     */
    private boolean borrowOrLend;
    //endregion

    /**
     * <Methods>
     *
     * @see Loan#getMoney()
     */
    //region Methods [getMoney]
    @Override
    public long getMoney() {
        return (borrowOrLend == BORROW ? 1 : -1) * money;
    }
    //endregion
}
