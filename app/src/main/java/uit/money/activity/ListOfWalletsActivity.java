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
import uit.money.R;
import uit.money.adapter.WalletRecyclerViewAdapter;
import uit.money.databinding.ActivityListOfWalletsBinding;

import static model.Const.BUY;
import static model.Utils.getMoney;

public class ListOfWalletsActivity extends RealmActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_wallets);

        initializeDataBinding();
    }

    private void initializeDataBinding() {
        final ActivityListOfWalletsBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_of_wallets);
        binding.setState(new State());
    }

    public void back(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void create(View view) {
        startActivity(new Intent(getBaseContext(), CreateWalletActivity.class));
    }

    public static class State extends Observable {
        public static final ObservableField<String> money = new ObservableField<>("");
        private User user = User.getCurrentUser();

        // TODO: make it sync
        State() {
            RealmResults<BillDetail> billDetails = Realm.getDefaultInstance()
                    .where(BillDetail.class)
                    .equalTo("bill.wallet.user.fbid", user.getFbid())
                    .findAllAsync();
            billDetails.removeAllChangeListeners();
            billDetails.addChangeListener(this::updateMoney);
            updateMoney(billDetails);
        }

        private void updateMoney(RealmResults<BillDetail> billDetails) {
            long money = 0;
            for (BillDetail billDetail : billDetails) {
                money += billDetail.getMoney() * (billDetail.getBill().isBuyOrSell() == BUY ? -1 : 1);
            }
            State.money.set(getMoney(money));
        }

        public WalletRecyclerViewAdapter getWalletAdapter() {
            return new WalletRecyclerViewAdapter(user);
        }
    }
}
