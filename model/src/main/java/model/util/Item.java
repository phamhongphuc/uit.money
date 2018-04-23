package model.util;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import model.deal.MultiLineDeal;

/**
 * Đại diện cho một dòng trong một cái hóa đơn
 * Ví dụ như: có 3 chai nước, mỗi chai có giá 10.000đ.
 */
public class Item extends RealmObject {
    public Item() {

    }

    /**
     * <Fields>
     *
     * @see Item#id                         {@link Integer}
     * @see Item#multiLineDeal              {@link MultiLineDeal}           >>  {@link MultiLineDeal#items}
     * @see Item#object                     {@link Object}                  >>  {@link Object#items}
     * @see Item#amount                     {@link Integer}
     * @see Item#unitPrice                  {@link Long}
     */
    //region Fields [id, multiLineDeal, object, amount, unitPrice]
    @PrimaryKey
    private int id;
    private MultiLineDeal multiLineDeal;
    private Object object;          // Đại diện cho [chai nước, cái ly, ...]

    private int amount;
    private long unitPrice;
    //endregion

    /**
     * <Methods>
     *
     * @see Item#getMoney()
     */
    //region Methods [getMoney]
    public long getMoney() {
        return amount * unitPrice;
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

    public MultiLineDeal getMultiLineDeal() {
        return multiLineDeal;
    }

    public void setMultiLineDeal(MultiLineDeal multiLineDeal) {
        this.multiLineDeal = multiLineDeal;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }
    //endregion
}
