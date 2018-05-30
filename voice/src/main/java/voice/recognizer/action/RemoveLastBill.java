    package voice.recognizer.action;

import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.Sort;
import model.model.Wallet;
import model.model.transaction.Bill;
import voice.InterfaceWalletActivity;
import voice.recognizer.InterfaceRecognizer;

import static voice.Utils.matchEachInput;

public class RemoveLastBill implements InterfaceRecognizer {
    private static final String TAG = "RemoveLastBill";
    private static final String regex = "xóa giao dịch gần nhất";

    @Override
    public boolean run(ArrayList<String> inputs, InterfaceWalletActivity activity) {
        final Realm realm = Realm.getDefaultInstance();
        final Wallet wallet = Wallet.getCurrentWallet();
        if (wallet == null) return false;

        if (!matchEachInput(inputs, regex)) {
            Log.d(TAG, "run() called with: inputs = [" + inputs + "], activity = [" + activity + "]");
            return false;
        }

        Bill bill = realm
                .where(Bill.class)
                .equalTo("wallet.id", wallet.getId())
                .sort("time", Sort.DESCENDING)
                .findFirst();

        if (bill == null) return false;

        realm.executeTransaction(r -> bill.deleteFromRealm());
        return true;
    }
}
