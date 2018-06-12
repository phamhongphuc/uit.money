package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import model.model.User;
import model.model.Wallet;
import model.model.transaction.Payment;
import uit.money.R;
import uit.money.databinding.ActivityCreateWalletBinding;

import static model.Const.IN;
import static model.model.transaction.Payment.INITIALIZE;

public class CreateWalletActivity extends RealmActivity {
    private static final int LAYOUT = R.layout.activity_create_wallet;
    private Wallet wallet = new Wallet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

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

                final String moneyString = wallet._money.get();
                if (moneyString == null || moneyString.equals("")) {
                    return;
                }
                final long money = Math.abs(Long.parseLong(moneyString.replaceAll("[^0-9]", "")));
                if (money == 0) {
                    return;
                }

                final Payment payment = new Payment();
                payment.autoId();
                payment.setKind(INITIALIZE);
                payment.setInOrOut(IN);
                payment.setWallet(wallet);
                payment.setMoney(money);
                r.copyToRealmOrUpdate(payment);
            });
            finish();
        }
    }

    private void initializeDataBinding() {
        final ActivityCreateWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        binding.setWallet(wallet);
    }
}
