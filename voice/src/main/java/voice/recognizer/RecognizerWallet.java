package voice.recognizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.Sort;
import model.model.Wallet;
import model.model.transaction.Bill;

public class RecognizerWallet {
    private final Wallet wallet;
    private final String input;
    private final boolean isValid;
    private Bill bill;

    public RecognizerWallet(Wallet wallet, String input) {
        this.wallet = wallet;
        this.input = input;
        isValid = getRemoveBill();
    }

    private boolean getRemoveBill() {
        Matcher matcher = Pattern.compile("xóa giao dịch gần nhất", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(input);
        if (!matcher.find()) return false;

        bill = Realm.getDefaultInstance()
                .where(Bill.class)
                .equalTo("wallet.id", wallet.getId())
                .sort("time", Sort.DESCENDING)
                .findFirst();
        return bill != null;
    }

    public void start() {
        if (!isValid) return;
        Realm.getDefaultInstance().executeTransaction(r -> {
            bill.deleteFromRealm();
        });
    }

    public boolean isValid() {
        return isValid;
    }
}
