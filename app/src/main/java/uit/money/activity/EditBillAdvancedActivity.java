package uit.money.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import model.model.Person;
import model.model.transaction.Bill;
import uit.money.R;
import uit.money.databinding.ActivityEditBillAdvancedBinding;

public class EditBillAdvancedActivity extends RealmActivity {
    private final int layout = R.layout.activity_edit_bill_advanced;
    private Bill bill = null;
    private State state = new State();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        initializeData();
        initializeDataBinding();
    }

    private void initializeData() {
        final int id = getIntent().getIntExtra("id", -1);
        if (id == -1) finish();

        bill = realm.where(Bill.class).equalTo("id", id).findFirst();
        if (bill == null) return;

        final Person with = bill.getWith();
        if (with != null) state.with.set(with.getName());

        state.location.set(bill.getLocation());
    }

    private void initializeDataBinding() {
        final ActivityEditBillAdvancedBinding binding;
        binding = DataBindingUtil.setContentView(this, layout);
        binding.setState(state);
    }

    public void back(View view) {
        finish();
    }

    public void done(View view) {
        realm.beginTransaction();

        bill.setLocation(state.getLocation());
        bill.setWith(state.getWith());

        realm.commitTransaction();
        finish();
    }

    public static class State extends Observable {
        public final ObservableField<String> location = new ObservableField<>("1");
        public final ObservableField<String> with = new ObservableField<>("");

        public String getLocation() {
            return location.get();
        }

        public Person getWith() {
            final String name = with.get();
            if (name == null || name.equals("")) {
                return null;
            } else {
                return new Person(name);
            }
        }
    }
}
