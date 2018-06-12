package voice.recognizer.open;

import java.util.ArrayList;

import voice.utils.InterfaceWalletActivity;
import voice.utils.InterfaceRecognizer;

import static voice.utils.Utils.matchEachInput;

public class OpenListOfWallet implements InterfaceRecognizer {
    private static final String REGEX = "mở danh sách ví";

    @Override
    public boolean run(ArrayList<String> inputs, InterfaceWalletActivity activity) {
        if (!matchEachInput(inputs, REGEX)) return false;

        activity.openListOfWallets(null);
        return true;
    }
}
