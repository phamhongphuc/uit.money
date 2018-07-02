package voice;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import voice.recognizer.action.CreateBill;
import voice.recognizer.action.RemoveLastBill;
import voice.recognizer.open.OpenCreateBill;
import voice.recognizer.open.OpenListOfWallet;
import voice.utils.InterfaceRecognizer;
import voice.utils.InterfaceWalletActivity;

import static java.util.Arrays.asList;

public class Recognizer {
    private static final String TAG = "Recognizer";
    private static final List<InterfaceRecognizer> recognizers = asList(
            new CreateBill(),
// TODO FIX THIS AS SOON AS POSSIBLE
//            new CreatePayment(),
            new RemoveLastBill(),
            new OpenListOfWallet(),
            new OpenCreateBill()
    );

    private final InterfaceWalletActivity activity;

    public Recognizer(InterfaceWalletActivity activity) {
        this.activity = activity;
    }

    public void recognize(ArrayList<String> texts) {
        for (InterfaceRecognizer recognizer : recognizers) {
            if (recognizer.run(texts, activity)) {
                Log.i(TAG, "recognize: something");
                return;
            }
        }
    }
}
