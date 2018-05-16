package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;

import io.realm.RealmResults;
import model.model.User;
import model.model.Wallet;
import model.model.transaction.Bill;
import uit.money.R;
import uit.money.databinding.ActivityEditWalletBinding;

public class EditWalletActivity extends RealmActivity {
    private State state;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        initializeWallet();
        initializeDataBinding();
    }

    private void initializeWallet() {
        wallet = Wallet.getCurrentWallet();
        if (wallet != null) {
            wallet.initialize();
        }
    }

    public void back(View view) {
        finish();
    }

    public void done(View view) {
        realm.executeTransaction(r -> wallet.updateName());
        finish();
    }

    private void initializeDataBinding() {
        final RealmResults<Bill> bills = wallet.getBills();
        bills.load();

        state = new State();
        state.count.set(bills.size());

        final ActivityEditWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_wallet);
        binding.setState(state);
        binding.setWallet(wallet);
    }

    public void deleteWallet(View view) {
        final RealmResults<Wallet> wallets = User.getCurrentUser().getWallets();
        wallets.load();
        if (wallets.size() > 1) {
            realm.executeTransaction(r -> wallet.deleteFromRealm());
            startActivity(new Intent(getBaseContext(), ListOfWalletsActivity.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), R.string.edit_wallet_delete_last_wallet, Toast.LENGTH_SHORT).show();
        }
    }

    public class State extends Observable {
        public final ObservableInt count = new ObservableInt(0);
    }
}
