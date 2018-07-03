package voice.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import voice.utils.Const.Key;

import static java.util.Arrays.asList;
import static voice.utils.Const.ALL_KEYS;

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
    public static Matcher getMatcher(String input, List<Key> keys, Key start) {
        List<String> stop = new ArrayList<>();

        for (Key k : keys) {
            if (k != start) {
                stop.addAll(ALL_KEYS.get(k));
            }
        }

        final String stopString = TextUtils.join("|", stop);
        String startString = TextUtils.join("|", ALL_KEYS.get(start));
        return getMatcher(input, startString, stopString);
    }

    @NonNull
    public static Matcher getMatcher(String input, String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input);
    }

    public static boolean matchEachInput(ArrayList<String> inputs, String regex) {
        for (String input : inputs) if (getMatcher(input, regex).find()) return true;
        return false;
    }

    public static String wordsOf(Key... listKeys) {
        List<String> keywords = new ArrayList<>();
        List<Key> keysList = asList(listKeys);
        for (Key k : keysList) {
            keywords.addAll(ALL_KEYS.get(k));
        }
        return TextUtils.join("|", keywords);
    }

    public static String wordsOfBut(List<Key> keysList, Key first) {
        List<String> keywords = new ArrayList<>();
        for (Key k : keysList) {
            if (k != first) {
                keywords.addAll(ALL_KEYS.get(k));
            }
        }
        return TextUtils.join("|", keywords);
    }

    @NonNull
    public static String getFirstFormattedText(ArrayList<String> rawText) {
        return getFormattedText(rawText.get(0));
    }

    @NonNull
    public static String getFormattedText(String rawText) {
        return rawText.replaceAll("\\.", "") + ".";
    }
}
