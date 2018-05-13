package model.model.util;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.model_model_util_ObjectRealmProxy;
import model.model.transaction.BillDetail;

/**
 * Quan hệ giữa Object và item là quan hệ 1-n
 * 1 Object thì nằm trong nhiều item
 * 1 item chỉ có 1 Object
 * <p>
 * Object là đại diện cho một mặt hàng
 * ví dụ như chai nước mắm, bịch đường.
 */

@Parcel(implementations = {model_model_util_ObjectRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Object.class})
public class Object extends RealmObject {
    public Object() {

    }

    /**
     * <Fields>
     *
     * @see Object#name                     {@link String}
     */
    //region Fields [name]
    @PrimaryKey
    private String name;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Object#billDetails
     */
    //region LinkingObjects [billDetails]
    @LinkingObjects("object")
    private final RealmResults<BillDetail> billDetails = null;

    public Object(String name) {
        this.name = name;
    }
    //endregion

    /**
     * <Getter> & <Setter>
     */
    //region Getter & Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmResults<BillDetail> getBillDetails() {
        return billDetails;
    }

    //endregion
}
