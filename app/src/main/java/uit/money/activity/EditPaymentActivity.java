package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.Wallet;
import model.model.transaction.Payment;
import uit.money.R;
import uit.money.databinding.ActivityEditPaymentBinding;

import static uit.money.activity.WalletActivity.CREATE;
import static uit.money.activity.WalletActivity.EDIT;
import static uit.money.activity.WalletActivity.ID;
import static uit.money.activity.WalletActivity.NONE;
import static uit.money.activity.WalletActivity.TYPE;

public class EditPaymentActivity extends RealmActivity {
    private static final int LAYOUT = R.layout.activity_edit_payment;

    private final State state = new State();
    private Payment payment = new Payment();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initializeData();
        initializeDataBinding();
    }

    private void initializeData() {
        final Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, NONE);
        switch (type) {
            case CREATE:
                state.title = getString(R.string.create_bill_title);
                realm.executeTransaction(r -> {
                    payment.autoId();
                    payment.setWallet(Wallet.getCurrentWallet());
                    payment = realm.copyToRealmOrUpdate(payment);
                });
                break;
            case EDIT:
                state.title = getString(R.string.edit_bill_title);
                final int id = intent.getIntExtra(ID, -1);
                payment = realm.where(Payment.class).equalTo("id", id).findFirst();
                if (payment == null) {
                    finish();
                    return;
                }
                break;
            default:
                finish();
                return;
        }
        payment.initialize();
    }

    private void initializeDataBinding() {
        final ActivityEditPaymentBinding binding;
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        binding.setState(state);
//        binding.setPayment(payment);
    }

    public void back(View view) {
        if (type == CREATE && payment.isValid()) payment.delete(realm);
        finish();
    }

    public void done(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (type == CREATE) payment.delete(realm);
        super.onBackPressed();
    }

    public static class State extends Observable {
        public final ObservableField<String> search = new ObservableField<>("");

        public String title = "";
    }
}
