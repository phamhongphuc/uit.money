package model.deal;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import model.Wallet;
import model.util.Organization;

/**
 * Dành cho các giao dịch chỉ có một dòng
 * Ví dụ như:
 * + Thanh toán tiền điện
 * + Nhận lương
 * + Thưởng
 * + ...
 */
public class SingleLineDeal extends RealmObject implements Deal {
    public SingleLineDeal() {
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
     * @see SingleLineDeal#id               {@link Integer}
     * @see SingleLineDeal#wallet           {@link Wallet}              >>  {@link Wallet#singleLineDeals}
     * @see SingleLineDeal#time             {@link Date}
     * @see SingleLineDeal#money            {@link Long}
     * @see SingleLineDeal#location         {@link String}
     * @see SingleLineDeal#organization     {@link Organization}        >>  {@link Organization#singleLineDeals}
     * @see SingleLineDeal#startDate        {@link Date}
     * @see SingleLineDeal#endDate          {@link Date}
     * @see SingleLineDeal#buyOrSell        {@link Boolean}
     */
    //region Fields [id, wallet, time, money, location, organization, startDate, endDate, buyOrSell]
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
    //endregion

    /**
     * <Methods>
     *
     * @see SingleLineDeal#getMoney()
     */
    //region Methods [getMoney]
    @Override
    public long getMoney() {
        return (buyOrSell == SELL ? 1 : -1) * money;
    }
    //endregion
}
