package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.transaction.Bill;
import uit.money.R;
import uit.money.databinding.ActivityBillBinding;

import static uit.money.activity.WalletActivity.EDIT;
import static uit.money.activity.WalletActivity.ID;
import static uit.money.activity.WalletActivity.TYPE;
import static uit.money.utils.Dialog.OpenConfirm;

public class BillActivity extends AppActivity {
    public static final int LAYOUT = R.layout.activity_bill;
    private ActivityBillBinding binding;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializeBill();
        initializeDataBinding();
    }

    private void initializeDataBinding() {
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        binding.setState(new State());
        binding.setBill(bill);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeBill();
        binding.setBill(bill);
    }

    private void initializeBill() {
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", -1);
        if (id == -1) finish();
        else {
            bill = realm.where(Bill.class).equalTo("id", id).findAll().first();
            if (bill != null) {
                bill.initialize();
            }
        }
    }

    public void back(View view) {
        finish();
    }

    public void deleteBill(View view) {
        OpenConfirm(this, getString(R.string.bill_delete_dialog), (() -> {
            realm.executeTransaction(r -> bill.deleteFromRealm());
            finish();
        }));
    }

    public void openEditBill(View view) {
        final Intent intent = new Intent(this, EditBillActivity.class);
        intent.putExtra(TYPE, EDIT);
        intent.putExtra(ID, bill.getId());
        delayStartActivity(intent);
    }

    public static class State extends Observable {

    }
}