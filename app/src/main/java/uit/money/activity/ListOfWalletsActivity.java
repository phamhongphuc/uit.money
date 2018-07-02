package uit.money.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import io.realm.Realm;
import io.realm.RealmResults;
import model.model.User;
import model.model.transaction.BillDetail;
import model.model.transaction.HasMoney;
import model.model.transaction.Loan;
import model.model.transaction.Payment;
import uit.money.R;
import uit.money.databinding.ActivityListOfWalletsBinding;

import static model.Utils.getMoney;

public class ListOfWalletsActivity extends RealmActivity {
    private static final int LAYOUT = R.layout.activity_list_of_wallets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializeDataBinding();
    }

    private void initializeDataBinding() {
        final ActivityListOfWalletsBinding binding;
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        binding.setState(new State(realm));
        binding.setUser(User.getCurrentUser());
    }

    public void back(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void create(View view) {
        startActivity(new Intent(this, CreateWalletActivity.class));
    }

    public static class State extends Observable {
        private final Realm realm;
        private final User user = User.getCurrentUser();

        public static final ObservableField<String> money = new ObservableField<>("");

        private long billDetailsMoney = 0L;
        private long paymentsMoney = 0L;
        private long loansMoney = 0L;

        State(Realm realm) {
            this.realm = realm;

            initialize(BillDetail.class, "bill.wallet.user.fbid");
            initialize(Payment.class, "wallet.user.fbid");
            initialize(Loan.class, "wallet.user.fbid");
            // Không cần cộng trừ Transfers vì chuyển qua chuyển lại
            // nhưng tổng tiền của người dùng vẫn luôn được bảo toàn
        }

        private void initialize(final Class<? extends HasMoney> clazz, final String fbid) {
            final RealmResults<? extends HasMoney> objects = realm
                    .where(clazz)
                    .equalTo(fbid, user.getFbid())
                    .findAllAsync();
            objects.load();
            objects.removeAllChangeListeners();
            objects.addChangeListener(hasMonies -> update(hasMonies, clazz));
            update(objects, clazz);
        }

        private void update(RealmResults<? extends HasMoney> hasMonies, Class<? extends HasMoney> clazz) {
            long money = 0;

            if (hasMonies.size() != 0) {
                for (HasMoney hasMoney : hasMonies) money += hasMoney.getMoney();
            }

            if (clazz == BillDetail.class) billDetailsMoney = money;
            else if (clazz == Payment.class) paymentsMoney = money;
            else if (clazz == Loan.class) loansMoney = money;

            updateMoney();
        }

        private void updateMoney() {
            State.money.set(getMoney(billDetailsMoney + paymentsMoney + loansMoney));
        }
    }
}
