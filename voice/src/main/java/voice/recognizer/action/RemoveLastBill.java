package voice.recognizer.action;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.Sort;
import model.model.Wallet;
import model.model.transaction.Bill;
import voice.InterfaceWalletActivity;
import voice.recognizer.InterfaceRecognizer;

import static voice.Utils.matchEachInput;

public class RemoveLastBill implements InterfaceRecognizer {
    private static final String regex = "xóa giao dịch gần nhất";

    @Override
    public boolean run(ArrayList<String> inputs, InterfaceWalletActivity activity) {
        final Wallet wallet = Wallet.getCurrentWallet();
        if (wallet == null) return false;

        if (!matchEachInput(inputs, regex)) return false;

        final Realm realm = Realm.getDefaultInstance();
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
