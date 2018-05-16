package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.User;
import model.model.Wallet;
import uit.money.R;
import uit.money.databinding.ActivityCreateWalletBinding;

public class CreateWalletActivity extends RealmActivity {

    private State state;

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
        Wallet wallet = new Wallet(w -> {
            w.autoId();
            w.setName(state.wallet.get());
            w.setUser(User.getCurrentUser());
        });

        realm.executeTransaction(r -> r.copyToRealmOrUpdate(wallet));
        finish();
    }

    private void initializeDataBinding() {
        state = new State();

        final ActivityCreateWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);
        binding.setState(state);
    }

    public class State extends Observable {
        public final ObservableField<String> money = new ObservableField<>("0 đ");
        public final ObservableField<String> wallet = new ObservableField<>("Tên ví mới");
    }
}
