package model.model;

import android.databinding.ObservableField;

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
import static model.Const.getMoney;

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
    @Ignore
    public final ObservableField<String> money = new ObservableField<>("");
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
        final User currentUser = User.getCurrentUser();
        if (currentUser == null) {
            return null;
        } else {
            final RealmResults<Wallet> wallets = currentUser.getWallets();
            if (wallets.size() == 0) {
                return Wallet.createEmptyWallet(currentUser);
            } else {
                return wallets.first();
            }
        }
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

    private void autoId() {
        RealmResults<Wallet> wallets = Realm.getDefaultInstance()
                .where(Wallet.class)
                .findAllAsync();
        wallets.load();
        id = wallets.size() == 0
                ? 0
                : Objects.requireNonNull(wallets.max("id")).intValue() + 1;
    }

    public void initialize() {
        if (billDetails == null) {
            billDetails = Realm.getDefaultInstance()
                    .where(BillDetail.class)
                    .equalTo("bill.wallet.id", id)
                    .findAllAsync();
        }
        billDetails.removeAllChangeListeners();
        billDetails.addChangeListener(this::updateMoney);
        updateMoney(billDetails);
    }

    private void updateMoney(RealmResults<BillDetail> billDetails) {
        long money = 0;
        for (BillDetail billDetail : billDetails) {
            money += billDetail.getMoney() * (billDetail.getBill().isBuyOrSell() == BUY ? -1 : 1);
        }
        this.money.set(getMoney(money));
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

    public interface Callback {
        void call(Wallet wallet);
    }
}