package uit.money.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;

import io.realm.RealmResults;
import model.model.User;
import model.model.transaction.BillDetail;
import model.model.transaction.Loan;
import model.model.transaction.Payment;
import uit.money.R;
import uit.money.adapter.WalletRecyclerViewAdapter;
import uit.money.databinding.ActivityListOfWalletsBinding;

import static model.Utils.getMoney;

public class ListOfWalletsActivity extends RealmActivity {

    private final int layout = R.layout.activity_list_of_wallets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        initializeDataBinding();
    }

    private void initializeDataBinding() {
        final ActivityListOfWalletsBinding binding;
        binding = DataBindingUtil.setContentView(this, layout);
        binding.setState(new State(this));
    }

    public void back(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void create(View view) {
        startActivity(new Intent(getBaseContext(), CreateWalletActivity.class));
    }

    public static class State extends Observable {
        public static final ObservableField<String> money = new ObservableField<>("");
        private long billDetailsMoney = 0;
        private long paymentsMoney;
        private long loansMoney;

        private User user = User.getCurrentUser();

        State(ListOfWalletsActivity activity) {
            initializeBillDetails(activity);
            initializePayments(activity);
            initializeLoan(activity);
            // Không cần cộng trừ Transfers vì chuyển qua chuyển lại
            // nhưng tổng tiền của người dùng vẫn luôn được bảo toàn
        }

        private void initializeBillDetails(ListOfWalletsActivity activity) {
            RealmResults<BillDetail> billDetails = activity.realm
                    .where(BillDetail.class)
                    .equalTo("bill.wallet.user.fbid", user.getFbid())
                    .findAllAsync();
            billDetails.load();
            billDetails.removeAllChangeListeners();
            billDetails.addChangeListener(this::updateBillDetails);
        }

        private void initializePayments(ListOfWalletsActivity activity) {
            final RealmResults<Payment> payments = activity.realm
                    .where(Payment.class)
                    .equalTo("wallet.user.fbid", user.getFbid())
                    .findAllAsync();
            payments.load();
            payments.removeAllChangeListeners();
            payments.addChangeListener(this::updatePayments);
            updatePayments(payments);
        }

        private void initializeLoan(ListOfWalletsActivity activity) {
            final RealmResults<Loan> loans = activity.realm
                    .where(Loan.class)
                    .equalTo("wallet.user.fbid", user.getFbid())
                    .findAllAsync();
            loans.load();
            loans.removeAllChangeListeners();
            loans.addChangeListener(this::updateLoans);
            updateLoans(loans);
        }

        private void updatePayments(RealmResults<Payment> payments) {
            paymentsMoney = 0;
            for (Payment p : payments) paymentsMoney += p.getMoney();
            updateMoney();
        }

        private void updateLoans(RealmResults<Loan> loans) {
            loansMoney = 0;
            for (Loan l : loans) loansMoney += l.getMoney();
            updateMoney();
        }

        private void updateMoney() {
            State.money.set(getMoney(billDetailsMoney + paymentsMoney + loansMoney));
        }

        private void updateBillDetails(RealmResults<BillDetail> billDetails) {
            billDetailsMoney = 0;
            for (BillDetail b : billDetails) billDetailsMoney += b.getMoney();
            updateMoney();
        }

        public WalletRecyclerViewAdapter getWalletAdapter() {
            return new WalletRecyclerViewAdapter(user);
        }
    }
}
