package uit.money.utils;

public class Money {

    public static long getMoneyNumber(String moneyString) {
        return Long.parseLong(moneyString.replaceAll("[^0-9]", ""));
    }
}
