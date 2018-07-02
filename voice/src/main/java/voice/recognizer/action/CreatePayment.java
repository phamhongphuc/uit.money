package voice.recognizer.action;

import android.util.Log;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import io.realm.Realm;
import model.model.Wallet;
import model.model.transaction.Payment;
import voice.getter.Getter;
import voice.utils.Const.Key;
import voice.utils.InterfaceRecognizer;
import voice.utils.InterfaceWalletActivity;

import static java.util.Arrays.asList;
import static model.Const.IN;
import static model.Const.OUT;
import static model.model.transaction.Payment.ELECTRIC_BILL;
import static model.model.transaction.Payment.HAVE_BREAKFAST;
import static model.model.transaction.Payment.HAVE_DINNER;
import static model.model.transaction.Payment.HAVE_LUNCH;
import static model.model.transaction.Payment.INTERNET_BILL;
import static model.model.transaction.Payment.PICK_LOST;
import static model.model.transaction.Payment.SALARY;
import static model.model.transaction.Payment.WATER_BILL;
import static voice.getter.Getter.getMoney;
import static voice.utils.Const.Key._ELECTRIC_BILL;
import static voice.utils.Const.Key._HAVE_BREAKFAST;
import static voice.utils.Const.Key._HAVE_DINNER;
import static voice.utils.Const.Key._HAVE_LUNCH;
import static voice.utils.Const.Key._INTERNET_BILL;
import static voice.utils.Const.Key._LOCATION;
import static voice.utils.Const.Key._LOST;
import static voice.utils.Const.Key._PICK;
import static voice.utils.Const.Key._SALARY;
import static voice.utils.Const.Key._TIME;
import static voice.utils.Const.Key._WATTER_BILL;
import static voice.utils.Utils.getFormattedText;
import static voice.utils.Utils.getMatcher;
import static voice.utils.Utils.wordsOf;

public class CreatePayment implements InterfaceRecognizer {
    private static final Map<Key, Integer> MAP = ImmutableMap.<Key, Integer>builder()
            .put(_PICK, PICK_LOST)
            .put(_LOST, -PICK_LOST)

            .put(_HAVE_BREAKFAST, -HAVE_BREAKFAST)
            .put(_HAVE_LUNCH, -HAVE_LUNCH)
            .put(_HAVE_DINNER, -HAVE_DINNER)

            .put(_SALARY, SALARY)

            .put(_INTERNET_BILL, -INTERNET_BILL)
            .put(_ELECTRIC_BILL, -ELECTRIC_BILL)
            .put(_WATTER_BILL, -WATER_BILL)

            .build();
    private static final List<Key> KEYS = asList(
            _PICK,
            _LOST,

            _HAVE_BREAKFAST,
            _HAVE_LUNCH,
            _HAVE_DINNER,

            _SALARY,

            _INTERNET_BILL,
            _ELECTRIC_BILL,
            _WATTER_BILL
    );

    @Override
    public boolean run(ArrayList<String> rawText, InterfaceWalletActivity activity) {
        Wallet wallet = Wallet.getCurrentWallet();
        if (wallet == null) return false;

        for (String text : rawText) {
            if (runEach(wallet, getFormattedText(text))) return true;
        }
        return false;
    }

    private boolean runEach(Wallet wallet, String text) {
        for (Key key : KEYS) {
            final Matcher matcher = getMatcher(text, wordsOf(key), wordsOf(_LOCATION, _TIME));
            if (matcher.find()) {
                Payment payment = new Payment();
                payment.setWallet(wallet);
                payment.setMoney(getMoney(matcher.group(2)));

                final int kind = MAP.get(key);
                payment.setKind(kind);
                payment.setInOrOut(kind > 0 ? IN : OUT);
                Log.i("", "run: " + kind);

                setLocation(text, payment, key);
                setTime(text, payment, key);

                save(payment);
                return true;
            }
        }

        return false;
    }

    private void save(Payment payment) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            payment.autoId();
            realm.copyToRealmOrUpdate(payment);
        });
    }

    private void setLocation(String text, Payment payment, Key keys) {
        final Matcher matcher_location = getMatcher(text, wordsOf(_LOCATION), wordsOf(keys, _TIME));
        if (matcher_location.find()) {
            payment.setLocation(matcher_location.group(2));
        }
    }

    private void setTime(String text, Payment payment, Key keys) {
        Date time = Getter.getTime(text, wordsOf(keys, _TIME));
        if (time != null) payment.setTime(time);
    }
}
