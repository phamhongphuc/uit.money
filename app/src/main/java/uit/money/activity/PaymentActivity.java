package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.transaction.Payment;
import uit.money.R;
import uit.money.databinding.ActivityPaymentBinding;

import static uit.money.activity.WalletActivity.EDIT;
import static uit.money.activity.WalletActivity.ID;
import static uit.money.activity.WalletActivity.TYPE;
import static uit.money.utils.Dialog.OpenConfirm;

public class PaymentActivity extends AppActivity {
    public static final int LAYOUT = R.layout.activity_payment;
    private ActivityPaymentBinding binding;
    private Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializePayment();
        initializeDataBinding();
    }

    private void initializePayment() {
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", -1);
        if (id == -1) finish();
        else {
            payment = realm.where(Payment.class).equalTo("id", id).findAll().first();
            if (payment != null) {
                payment.initialize();
            }
        }
    }

    private void initializeDataBinding() {
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        binding.setState(new State());
        binding.setPayment(payment);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializePayment();
        binding.setPayment(payment);
    }

    public void back(View view) {
        finish();
    }

    public void deletePayment(View view) {
        OpenConfirm(this, getString(R.string.bill_delete_dialog), (() -> {
            realm.executeTransaction(r -> payment.deleteFromRealm());
            finish();
        }));
    }

    public void openEditPayment(View view) {
        final Intent intent = new Intent(this, EditPaymentActivity.class);
        intent.putExtra(TYPE, EDIT);
        intent.putExtra(ID, payment.getId());
        delayStartActivity(intent);
    }

    public static class State extends Observable {

    }
}