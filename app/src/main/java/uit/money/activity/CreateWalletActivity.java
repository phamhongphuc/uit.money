package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import model.model.User;
import model.model.Wallet;
import uit.money.R;
import uit.money.databinding.ActivityCreateWalletBinding;

public class CreateWalletActivity extends RealmActivity {
    private Wallet wallet = new Wallet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        initializeDataBinding();
    }

    public void back(View view) {
        finish();
    }

    public void done(View view) {
        realm.executeTransaction(r -> {
            wallet.autoId();
            wallet.updateName();
            wallet.setUser(User.getCurrentUser());

            r.copyToRealmOrUpdate(wallet);
        });
        finish();
    }

    private void initializeDataBinding() {
        final ActivityCreateWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);
        binding.setWallet(wallet);
    }
}
