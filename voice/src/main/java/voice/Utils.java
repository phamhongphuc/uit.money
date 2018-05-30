package voice;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static int getInt(String number) {
        return Integer.parseInt(
                number
                        .replaceAll("một", "1")
                        .replaceAll("hai", "2")
                        .replaceAll("ba", "3")
                        .replaceAll("bốn", "4")
                        .replaceAll("năm", "5")
        );
    }

    @NonNull
    public static Matcher getMatcher(String input, String start, String stop) {
        return getMatcher(input, String.format("(%s) (([^\\r\\n](?!(%s)))+)[ \\.]", start, stop));
    }

    @NonNull
    public static Matcher getMatcher(String input, String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input);
    }

    public static boolean matchEachInput(ArrayList<String> inputs, String regex) {
        for (String input : inputs) if (getMatcher(input, regex).find()) return true;
        return false;
    }
}
