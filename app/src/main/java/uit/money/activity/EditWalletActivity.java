package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import io.realm.RealmResults;
import model.model.User;
import model.model.Wallet;
import uit.money.R;
import uit.money.databinding.ActivityEditWalletBinding;

public class EditWalletActivity extends RealmActivity {
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
        if (wallet._name.get() == null || Objects.requireNonNull(wallet._name.get()).equals("")) {
            Toast.makeText(getApplicationContext(), R.string.error_empty_wallet_name, Toast.LENGTH_SHORT).show();
        } else {
            realm.executeTransaction(r -> wallet.updateName());
            finish();
        }
    }

    private void initializeDataBinding() {
        final ActivityEditWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_wallet);
        binding.setWallet(wallet);
    }

    public void deleteWallet(View view) {
        final RealmResults<Wallet> wallets = User.getCurrentUser().getWallets();
        wallets.load();
        if (wallets.size() > 1) {
            realm.executeTransaction(r -> {
                wallet.getBillDetails().deleteAllFromRealm();
                wallet.getBills().deleteAllFromRealm();
                wallet.getLoans().deleteAllFromRealm();
                wallet.getPayments().deleteAllFromRealm();
                wallet.getTransfers().deleteAllFromRealm();
                wallet.deleteFromRealm();
            });
            startActivity(new Intent(getBaseContext(), ListOfWalletsActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.edit_wallet_delete_last_wallet, Toast.LENGTH_SHORT).show();
        }
    }
}
