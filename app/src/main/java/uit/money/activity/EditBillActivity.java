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
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final int NONE = 0;
    public static final int CREATE = 1;
    public static final int EDIT = 2;

    private final int layout = R.layout.activity_edit_bill;
    private Bill bill = new Bill();
    private State state = new State();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        initializeData();
        initializeDataBinding();
    }

    private void initializeData() {
        final Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, NONE);
        switch (type) {
            case CREATE:
                realm.beginTransaction();

                bill.autoId();
                bill.setWallet(Wallet.getCurrentWallet());
                bill = realm.copyToRealmOrUpdate(bill);

                realm.commitTransaction();
                break;
            case EDIT:
                final int id = intent.getIntExtra(ID, -1);
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
        binding = DataBindingUtil.setContentView(this, layout);
        binding.setState(state);
        binding.setBill(bill);
    }

    public void back(View view) {
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
            detail.setAmount(state.getIntAmount());
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
        if (type == CREATE) bill.delete(realm);
        super.onBackPressed();
    }

    public static class State extends Observable {
        public final ObservableField<String> objectName = new ObservableField<>("Tên món đồ");
        public final ObservableField<String> amount = new ObservableField<>("1");
        public final ObservableField<String> unitPrice = new ObservableField<>("1.000");

        long getLongUnitPrice() {
            String s = unitPrice.get();
            if (s == null) s = "0";
            s = s.replaceAll("[^0-9]", "");
            if (s.equals("")) s = "0";

            return Long.parseLong(s);
        }

        int getIntAmount() {
            return Integer.parseInt(amount.get());
        }

        String getStringObjectName() {
            String s = objectName.get();
            if (s == null) s = "null";
            return s;
        }
    }
}
