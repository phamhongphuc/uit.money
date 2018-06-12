package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.transaction.Bill;
import uit.money.R;
import uit.money.databinding.ActivityBillBinding;

import static uit.money.activity.EditBillActivity.EDIT;
import static uit.money.activity.EditBillActivity.ID;
import static uit.money.activity.EditBillActivity.TYPE;

public class BillActivity extends RealmActivity {
    private static final String TAG = "BillActivity";
    private ActivityBillBinding binding;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        initializeBill();
        initializeDataBinding();
    }

    private void initializeDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill);
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

    public void removeBill(View view) {
        realm.executeTransaction(r -> bill.deleteFromRealm());
        finish();
    }

    public void editBill(View view) {
        final Intent intent = new Intent(getBaseContext(), EditBillActivity.class);
        intent.putExtra(TYPE, EDIT);
        intent.putExtra(ID, bill.getId());
        startActivity(intent);
    }

    public static class State extends Observable {

    }
}