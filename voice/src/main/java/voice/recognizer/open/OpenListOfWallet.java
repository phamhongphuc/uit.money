package voice.recognizer.open;

import android.util.Log;

import java.util.ArrayList;

import voice.InterfaceWalletActivity;
import voice.recognizer.InterfaceRecognizer;

import static voice.Utils.matchEachInput;

public class OpenListOfWallet implements InterfaceRecognizer {
    private static final String TAG = "OpenListOfWallet";
    private static final String REGEX = "mở danh sách ví";

    @Override
    public boolean run(ArrayList<String> inputs, InterfaceWalletActivity activity) {
        if (!matchEachInput(inputs, REGEX)) {
            Log.i(TAG, "run() called with: inputs = [" + inputs + "], activity = [" + activity + "]");
            return false;
        }

        activity.openListOfWallets(null);
        return true;
    }
}
