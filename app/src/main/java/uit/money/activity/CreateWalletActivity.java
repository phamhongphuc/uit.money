package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

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
        if (wallet._name.get() == null || Objects.requireNonNull(wallet._name.get()).equals("")) {
            Toast.makeText(getApplicationContext(), R.string.error_empty_wallet_name, Toast.LENGTH_SHORT).show();
        } else {
            realm.executeTransaction(r -> {
                wallet.autoId();
                wallet.updateName();
                wallet.setUser(User.getCurrentUser());
                r.copyToRealmOrUpdate(wallet);
            });
            finish();
        }
    }

    private void initializeDataBinding() {
        final ActivityCreateWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);
        binding.setWallet(wallet);
    }
}
