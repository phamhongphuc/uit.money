package voice.recognizer;

import java.util.ArrayList;

import voice.InterfaceWalletActivity;

public interface InterfaceRecognizer {
    boolean run(ArrayList<String> text, InterfaceWalletActivity activity);
}
