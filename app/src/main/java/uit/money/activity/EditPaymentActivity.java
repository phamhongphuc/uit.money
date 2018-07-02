package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;
import java.util.Observable;

import model.model.Wallet;
import model.model.transaction.Payment;
import uit.money.R;
import uit.money.adapter.PaymentTypeRecyclerViewAdapter;
import uit.money.databinding.ActivityEditPaymentBinding;

import static uit.money.activity.WalletActivity.CREATE;
import static uit.money.activity.WalletActivity.EDIT;
import static uit.money.activity.WalletActivity.ID;
import static uit.money.activity.WalletActivity.NONE;
import static uit.money.activity.WalletActivity.TYPE;
import static uit.money.adapter.PaymentTypeRecyclerViewAdapter.states;
import static uit.money.utils.Money.getMoneyNumber;

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
    }

    public void back(View view) {
        if (type == CREATE && payment.isValid()) payment.delete(realm);
        finish();
    }

    public void done(View view) {
        for (PaymentTypeRecyclerViewAdapter.State type : states) {
            if (type.iconColor.get() == getColor(R.color.in_color)) {
                realm.executeTransaction(r -> {
                    payment.setKind(type.kind);
                    payment.setMoney(getMoneyNumber(Objects.requireNonNull(state.money.get())));
                });
                finish();
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (type == CREATE) payment.delete(realm);
        super.onBackPressed();
    }

    public static class State extends Observable {
        public String title = "";
        public ObservableField<String> money = new ObservableField<>("");
    }
}
