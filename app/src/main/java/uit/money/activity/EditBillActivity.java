package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;

import io.realm.RealmResults;
import model.model.Wallet;
import model.model.transaction.Bill;
import model.model.transaction.BillDetail;
import model.model.util.Object;
import uit.money.R;
import uit.money.databinding.ActivityEditBillBinding;

public class EditBillActivity extends RealmActivity {
    private static final int LAYOUT = R.layout.activity_edit_bill;

    private final State state = new State();
    private Bill bill = new Bill();
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
        type = intent.getIntExtra(WalletActivity.TYPE, WalletActivity.NONE);
        switch (type) {
            case WalletActivity.CREATE:
                state.title = getString(R.string.create_bill_title);
                realm.executeTransaction(r -> {
                    bill.autoId();
                    bill.setWallet(Wallet.getCurrentWallet());
                    bill = realm.copyToRealmOrUpdate(bill);
                });
                break;
            case WalletActivity.EDIT:
                state.title = getString(R.string.edit_bill_title);
                final int id = intent.getIntExtra(WalletActivity.ID, -1);
                bill = realm.where(Bill.class).equalTo("id", id).findFirst();
                if (bill == null) {
                    finish();
                    return;
                }
                break;
            default:
                finish();
                return;
        }
        bill.initialize();
    }

    private void initializeDataBinding() {
        final ActivityEditBillBinding binding;
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        binding.setState(state);
        binding.setBill(bill);
    }

    public void back(View view) {
        if (type == WalletActivity.CREATE && bill.isValid()) {
            realm.executeTransaction(r -> {
                bill.getBillDetails().deleteAllFromRealm();
                bill.deleteFromRealm();
            });
        }
        finish();
    }

    public void done(View view) {
        final RealmResults<BillDetail> billDetails = bill.getBillDetails();
        billDetails.load();
        if (billDetails.size() == 0) {
            Toast.makeText(getApplicationContext(), R.string.cant_create_empty_bill, Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
    }

    public void add(View view) {
        final String stringObjectName = state.getStringObjectName();
        if (stringObjectName.length() < 3) {
            Toast.makeText(getApplicationContext(), R.string.object_name_must_have_more_than_3_characters, Toast.LENGTH_SHORT).show();
            return;
        }

        realm.executeTransaction(r -> {
            final Object object = new Object(stringObjectName);

            final BillDetail detail = new BillDetail();
            detail.autoId();
            detail.setBill(bill);
            detail.setObject(object);
            detail.setQuantity(state.getIntQuantity());
            detail.setUnitPrice(state.getLongUnitPrice());

            r.copyToRealmOrUpdate(object);
            r.copyToRealmOrUpdate(detail);
        });
    }

    public void clear(View view) {
        state.objectName.set("");
    }

    public void goToAdvanced(View view) {
        final Intent intent = new Intent(getBaseContext(), EditBillAdvancedActivity.class);
        intent.putExtra("id", bill.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (type == WalletActivity.CREATE) bill.delete(realm);
        super.onBackPressed();
    }

    public static class State extends Observable {
        public final ObservableField<String> objectName = new ObservableField<>("Tên món đồ");
        public final ObservableField<String> quantity = new ObservableField<>("1");
        public final ObservableField<String> unitPrice = new ObservableField<>("1.000");
        public String title = "";

        long getLongUnitPrice() {
            String s = unitPrice.get();
            if (s == null) s = "0";
            s = s.replaceAll("[^0-9]", "");
            if (s.equals("")) s = "0";

            return Long.parseLong(s);
        }

        int getIntQuantity() {
            return Integer.parseInt(quantity.get());
        }

        String getStringObjectName() {
            String s = objectName.get();
            if (s == null) s = "null";
            return s;
        }
    }
}
