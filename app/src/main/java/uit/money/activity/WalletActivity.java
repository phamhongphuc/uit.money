package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import org.parceler.Parcels;

import model.model.Wallet;
import uit.money.R;
import uit.money.databinding.ActivityWalletBinding;

public class WalletActivity extends RealmActivity {
    private static final String TAG = "WalletActivity";
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initializeWallet();
        initializeDataBinding();
    }

    private void initializeWallet() {
        realm.beginTransaction();
        wallet = realm.copyToRealmOrUpdate((Wallet) Parcels.unwrap(getIntent().getParcelableExtra("wallet")));
        realm.commitTransaction();
        wallet.initialize();
    }

    private void initializeDataBinding() {
        final ActivityWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        binding.setWallet(wallet);
    }
}