package model.util;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import model.Wallet;

/**
 * Quan hệ giữa Object và Item là quan hệ 1-n
 * 1 Object thì nằm trong nhiều Item
 * 1 Item chỉ có 1 Object
 * <p>
 * Object là đại diện cho một mặt hàng
 * ví dụ như chai nước mắm, bịch đường.
 */
public class Object extends RealmObject {
    public Object() {

    }

    /**
     * <Fields>
     *
     * @see String#name                     {@link String}
     */
    //region Fields [name]
    @PrimaryKey
    private String name;
    //endregion

    /**
     * <LinkingObjects>
     *
     * @see Object#items
     */
    //region LinkingObjects [items]
    @LinkingObjects("object")
    private final RealmResults<Item> items = null;
    //endregion
}
