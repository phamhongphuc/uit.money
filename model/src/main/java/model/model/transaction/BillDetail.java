package model.model.transaction;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.model_model_transaction_BillDetailRealmProxy;
import model.model.util.Object;

//import io.realm.model_transaction_BillDetailRealmProxy;

/**
 * Đại diện cho một dòng trong một cái hóa đơn
 * Ví dụ như: có 3 chai nước, mỗi chai có giá 10.000đ.
 * <Fields>
 *
 * @see BillDetail#id                          {@link Integer}
 * @see BillDetail#bill                        {@link Bill}                >>  {@link Bill#billDetails}
 * @see BillDetail#object                      {@link Object}              >>  {@link Object#billDetails}
 * @see BillDetail#amount                      {@link Integer}
 * @see BillDetail#unitPrice                   {@link Long}
 */
@Parcel(implementations = {model_model_transaction_BillDetailRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {BillDetail.class})
public class BillDetail extends RealmObject {
    @PrimaryKey
    private int id;
    private Bill bill;
    private Object object;          // Đại diện cho [chai nước, cái ly, ...]
    private int amount;
    private long unitPrice;

    public BillDetail() {

    }

    public long getMoney() {
        return amount * unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
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
}
