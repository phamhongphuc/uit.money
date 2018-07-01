package voice.recognizer.action;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import io.realm.Realm;
import model.model.Person;
import model.model.Wallet;
import model.model.transaction.Bill;
import model.model.transaction.BillDetail;
import model.model.util.Object;
import voice.getter.Getter;
import voice.utils.Const.Key;
import voice.utils.InterfaceRecognizer;
import voice.utils.InterfaceWalletActivity;
import voice.utils.Utils;

import static java.util.Arrays.asList;
import static model.Const.IN;
import static model.Const.OUT;
import static voice.utils.Const.Key._BUY_SELL;
import static voice.utils.Const.Key._LOCATION;
import static voice.utils.Const.Key._PRICE;
import static voice.utils.Const.Key._TIME;
import static voice.utils.Const.Key._WITH;
import static voice.utils.Utils.getMatcher;
import static voice.utils.Utils.wordsOfBut;

public class CreateBill implements InterfaceRecognizer {
    private static final List<Key> KEYS = asList(_BUY_SELL, _PRICE, _LOCATION, _TIME, _WITH);

    @Override
    public boolean run(ArrayList<String> rawText, InterfaceWalletActivity activity) {
        Wallet wallet = Wallet.getCurrentWallet();
        if (wallet == null) return false;

        String text = Utils.getFirstFormattedText(rawText);

        final Matcher matcher_buy_sell = getMatcher(
                text,
                "(mua|sắm)|(bán)",
                wordsOfBut(KEYS, _BUY_SELL)
        );
        if (!matcher_buy_sell.find()) return false;

        Bill bill = new Bill(wallet);
        String buy = matcher_buy_sell.group(2);
        String sell = matcher_buy_sell.group(3);
        if (!setBuyOrSell(bill, buy, sell)) return false;

        String quantity_object = matcher_buy_sell.group(4);
        BillDetail billDetail = setBillDetail(bill, quantity_object);
        if (billDetail == null) return false;

        if (!setUnitPrice(text, billDetail)) return false;

        setLocation(text, bill);
        setWith(text, bill);
        setTime(text, bill);

        save(bill, billDetail);


        TextToSpeech textToSpeech = new TextToSpeech(((AppCompatActivity) activity).getApplicationContext(), status -> {

        });
        textToSpeech.speak("Đã tạo giao dịch thành công", TextToSpeech.QUEUE_FLUSH, null, null);

        return true;
    }

    private boolean setBuyOrSell(Bill bill, String buy, String sell) {
        // OUT OR IN
        if (buy != null) {
            bill.setInOrOut(OUT);
            return true;
        } else if (sell != null) {
            bill.setInOrOut(IN);
            return true;
        } else {
            return false;
        }
    }

    private BillDetail setBillDetail(Bill bill, String quantity_object) {
        BillDetail detail = new BillDetail();
        detail.setBill(bill);

        final Matcher matcher_quantity = getMatcher(quantity_object, "(một|hai|ba|\\d+) ([^\\r\\n]+)");
        if (!matcher_quantity.find()) return null;
        detail.setQuantity(Utils.getInt(matcher_quantity.group(1)));

        Object object = new Object(matcher_quantity.group(2).replaceAll("^(cái|chiếc) ", ""));
        detail.setObject(object);

        return detail;
    }

    private boolean setUnitPrice(String text, BillDetail billDetail) {
        final Matcher matcher_price = getMatcher(text, KEYS, _PRICE);
        if (!matcher_price.find()) return false;

        final long unitPrice = Getter.getMoney(matcher_price.group(2));
        billDetail.setUnitPrice(unitPrice);
        return true;
    }

    private void setLocation(String text, Bill bill) {
        final Matcher matcher_location = getMatcher(text, KEYS, _LOCATION);
        if (matcher_location.find()) {
            bill.setLocation(matcher_location.group(2));
        }
    }

    private void setWith(String text, Bill bill) {
        final Matcher matcher_with = getMatcher(text, KEYS, _WITH);
        if (matcher_with.find()) {
            Person person = new Person(matcher_with.group(2));
            bill.setWith(person);
        }
    }

    private void setTime(String text, Bill bill) {
        Date time = Getter.getTime(text, wordsOfBut(KEYS, _TIME));
        if (time != null) bill.setTime(time);
    }

    private void save(Bill bill, BillDetail billDetail) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            bill.autoId();
            billDetail.autoId();

            r.copyToRealmOrUpdate(bill);
            r.copyToRealmOrUpdate(billDetail);
            r.copyToRealmOrUpdate(billDetail.getObject());

            if (bill.getWith() != null) r.copyToRealmOrUpdate(bill.getWith());
        });
    }

}
