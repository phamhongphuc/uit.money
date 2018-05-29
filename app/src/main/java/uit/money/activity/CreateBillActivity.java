package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.Wallet;
import model.model.transaction.Bill;
import model.model.transaction.BillDetail;
import model.model.util.Object;
import uit.money.R;
import uit.money.databinding.ActivityCreateBillBinding;

public class CreateBillActivity extends RealmActivity {
    private final int layout = R.layout.activity_create_bill;
    private Bill bill = new Bill();
    private State state = new State();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        initializeData();
        initializeDataBinding();
    }

    private void initializeData() {
        realm.beginTransaction();
        bill.autoId();
        bill.setWallet(Wallet.getCurrentWallet());
        realm.copyToRealmOrUpdate(bill);
        realm.commitTransaction();
        bill.initialize();
    }

    private void initializeDataBinding() {
        final ActivityCreateBillBinding binding;
        binding = DataBindingUtil.setContentView(this, layout);
        binding.setState(state);
        binding.setBill(bill);
    }

    public void back(View view) {
        finish();
    }

    public void done(View view) {

    }

    public void add(View view) {
        realm.executeTransaction(r -> {
            final Object object = new Object(state.getStringObjectName());

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
