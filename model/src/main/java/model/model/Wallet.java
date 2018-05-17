package model.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import org.parceler.Parcel;

import java.util.Objects;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model_model_WalletRealmProxy;
import model.model.transaction.Bill;
import model.model.transaction.BillDetail;
import model.model.transaction.Loan;

import static model.Const.BUY;
import static model.Utils.getMoney;

/**
 * <Fields>
 *
 * @see Wallet#id                       {@link Integer}
 * @see Wallet#name                     {@link String}
 * @see Wallet#user                     {@link User}                >>  {@link User#wallets}
 */
@Parcel(implementations = {model_model_WalletRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Wallet.class})
public class Wallet extends RealmObject {
    private static Wallet currentWallet = null;

    // TODO: Binding Error when String is "" or null
    @Ignore
    public final ObservableField<String> _money = new ObservableField<>("");
    @Ignore
    public final ObservableField<String> _name = new ObservableField<>("");
    @Ignore
    public final ObservableInt _count = new ObservableInt(0);

    @PrimaryKey
    private int id;
    @Required
    private String name;
    private User user;
    @Ignore
    private RealmResults<Bill> bills = null;
    @Ignore
    private RealmResults<BillDetail> billDetails = null;

    public Wallet() {

    }

    public Wallet(Callback callback) {
        callback.call(this);
    }

    @Nullable
    public static Wallet getCurrentWallet() {
        if (currentWallet == null) {
            final User currentUser = User.getCurrentUser();
            if (currentUser == null) {
                return null;
            } else {
                final RealmResults<Wallet> wallets = currentUser.getWallets();
                wallets.load();
                if (wallets.size() == 0) {
                    return Wallet.createEmptyWallet(currentUser);
                } else {
                    return wallets.first();
                }
            }
        } else {
            return currentWallet;
        }
    }

    public static void setCurrentWallet(Wallet currentWallet) {
        Wallet.currentWallet = currentWallet;
    }

    private static Wallet createEmptyWallet(User user) {
        Realm realm = Realm.getDefaultInstance();
        Wallet wallet = new Wallet(w -> {
            w.autoId();
            w.setName("Ví");
            w.setUser(user);
        });
        realm.beginTransaction();

        Wallet managedWallet = realm.copyToRealmOrUpdate(wallet);
        realm.commitTransaction();
        return managedWallet;
    }

    public void autoId() {
        RealmResults<Wallet> wallets = Realm.getDefaultInstance()
                .where(Wallet.class)
                .findAllAsync();
        wallets.load();
        id = wallets.size() == 0
                ? 0
                : Objects.requireNonNull(wallets.max("id")).intValue() + 1;
    }

    public void initialize() {
        _name.set(getName());

        if (billDetails == null) {
            billDetails = Realm.getDefaultInstance()
                    .where(BillDetail.class)
                    .equalTo("bill.wallet.id", id)
                    .findAllAsync();
        }
        billDetails.removeAllChangeListeners();
        billDetails.addChangeListener(this::updateBillDetails);
        updateBillDetails(billDetails);
    }

    private void updateBillDetails(RealmResults<BillDetail> billDetails) {
        long money = 0;
        for (BillDetail billDetail : billDetails) {
            money += billDetail.getMoney() * (billDetail.getBill().isBuyOrSell() == BUY ? -1 : 1);
        }
        _money.set(getMoney(money));
    }

    public RealmResults<Bill> getBills() {
        if (bills == null) {
            bills = Realm.getDefaultInstance()
                    .where(Bill.class)
                    .equalTo("wallet.id", id)
                    .findAllAsync();
        }
        return bills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        _name.set(name);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealmResults<Loan> getLoans() {
        return Realm.getDefaultInstance()
                .where(Loan.class)
                .equalTo("wallet.id", id)
                .findAllAsync();
    }

    public void updateName() {
        name = _name.get();
    }

    public interface Callback {
        void call(Wallet wallet);
    }
}