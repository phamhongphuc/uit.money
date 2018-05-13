package voice.recognizer;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.model.Person;
import model.model.transaction.BillDetail;
import model.model.util.Object;

import static java.util.Arrays.asList;
import static model.Const.BUY;
import static model.Const.SELL;

public class Bill {
    // BUY SELL
    private static final Map<Keys, List<String>> BUY_SELL_KEYS = ImmutableMap.<Keys, List<String>>builder()
            .put(Keys.BUY_SELL, asList("mua", "bán", "sắm"))
            .put(Keys.PRICE, asList("hết", "tốn", "với giá", "mất", "giá"))
            .put(Keys.LOCATION, asList("tại", "ở", "ngoài", "ở ngoài"))
            .put(Keys.TIME, asList("lúc", "vào lúc", "hồi", "ngày"))
            .put(Keys.WITH, asList("đi cùng", "cùng", "cùng với", "đi với"))
            .build();

    public static model.model.transaction.Bill getBill(String input) {
        final Matcher matcher_buy_sell = getMatcher(
                input,
                "(mua|sắm)|(bán)",
                joinKeywords(Keys.PRICE, Keys.LOCATION, Keys.TIME, Keys.WITH)
        );
        if (!matcher_buy_sell.find()) return null;

        model.model.transaction.Bill bill = new model.model.transaction.Bill();

        String buy = matcher_buy_sell.group(2);
        String sell = matcher_buy_sell.group(3);
        if (!setBuyOrSell(bill, buy, sell)) return null;

        String amount_object = matcher_buy_sell.group(4);
        final BillDetail detail = setBillDetail(bill, amount_object);
        if (detail == null) return null;

        // PRICE
        if (!setUnitPrice(input, detail)) return null;

        // LOCATION
        setLocation(input, bill);

        // WITH
        setWith(input, bill);

        // TIME
        setTime(input, bill);

        return bill;
    }

    @NonNull
    private static Matcher getMatcher(String input, String start, String stop) {
        String matcherString = String.format("(%s) (([^\\r\\n](?!(%s)))+)[ \\.]", start, stop);
        return Pattern.compile(matcherString, Pattern.CASE_INSENSITIVE).matcher(input);
    }

    private static String joinKeywords(Keys... listKeys) {
        List<String> keywords = new ArrayList<>();
        List<Keys> keysList = asList(listKeys);
        for (Keys k : keysList) {
            keywords.addAll(BUY_SELL_KEYS.get(k));
        }
        return TextUtils.join("|", keywords);
    }

    private static boolean setBuyOrSell(model.model.transaction.Bill bill, String buy, String sell) {
        // BUY OR SELL
        if (buy != null) {
            bill.setBuyOrSell(BUY);
            return true;
        } else if (sell != null) {
            bill.setBuyOrSell(SELL);
            return true;
        } else {
            return false;
        }
    }

    private static BillDetail setBillDetail(model.model.transaction.Bill bill, String amount_object) {
        BillDetail detail = new BillDetail();
        detail.setBill(bill);

        final Matcher matcher_amount = getMatcher(amount_object, "((một)|(hai)|(ba)|(\\d+)) ([^\\r\\n]+)");
        if (!matcher_amount.find()) return null;

        int amount = 0;
        if (matcher_amount.group(2) != null) amount = 1;
        else if (matcher_amount.group(3) != null) amount = 2;
        else if (matcher_amount.group(4) != null) amount = 3;
        else if (matcher_amount.group(5) != null) {
            amount = Integer.parseInt(matcher_amount.group(5));
        }
        detail.setAmount(amount);
        detail.setObject(new Object(matcher_amount.group(6)));

        return detail;
    }

    private static boolean setUnitPrice(String input, BillDetail detail) {
        final Matcher matcher_price = getMatcher(
                input,
                joinKeywords(Keys.PRICE),
                joinKeywords(Keys.LOCATION, Keys.TIME, Keys.WITH, Keys.BUY_SELL)
        );
        if (!matcher_price.find()) return false;
        final String number = matcher_price.group(2)
                .replaceAll("[^0-9]", "");
        detail.setUnitPrice(Long.parseLong(number));
        return true;
    }

    private static void setLocation(String input, model.model.transaction.Bill bill) {
        final Matcher matcher_location = getMatcher(
                input,
                joinKeywords(Keys.LOCATION),
                joinKeywords(Keys.BUY_SELL, Keys.PRICE, Keys.TIME, Keys.WITH)
        );
        if (matcher_location.find()) {
            bill.setLocation(matcher_location.group(2));
        }
    }

    private static void setWith(String input, model.model.transaction.Bill bill) {
        final Matcher matcher_with = getMatcher(
                input,
                joinKeywords(Keys.WITH),
                joinKeywords(Keys.BUY_SELL, Keys.PRICE, Keys.TIME, Keys.LOCATION)
        );
        if (matcher_with.find()) {
            bill.setWith(new Person(matcher_with.group(2)));
        }
    }

    private static void setTime(String input, model.model.transaction.Bill bill) {
        Date time = getTime(input, joinKeywords(Keys.BUY_SELL, Keys.PRICE, Keys.WITH, Keys.LOCATION));
        if (time != null) bill.setTime(time);
    }

    @NonNull
    private static Matcher getMatcher(String input, String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input);
    }

    private static Date getTime(String input, String stop) {
        final Matcher matcher_time = getMatcher(
                input, "(vào|vào lúc|lúc) ([^\\r\\n]+)|ngày (hôm qua)|(hôm kia)", stop
        );
        if (matcher_time.find()) {
            final String yesterday = matcher_time.group(4);
            final String the_day_before_yesterday = matcher_time.group(5);
            final String at = matcher_time.group(2);

            final Calendar cal = Calendar.getInstance();
            if (yesterday != null) {
                cal.add(Calendar.DATE, -1);
            } else if (the_day_before_yesterday != null) {
                cal.add(Calendar.DATE, -2);
            } else if (at != null) {
                final Matcher matcher_at = getMatcher(
                        at, "((\\d+) giờ trước|(\\d+) ngày trước|((\\d+):(\\d+)|(\\d+) giờ)( (\\d+) ngày trước|( ngày hôm qua)|( ngày hôm kia))?)"
                );
                if (matcher_at.find()) return null;
                final String hour_ago = matcher_at.group(2);
                final String date_ago = matcher_at.group(3);
                final String date_time = matcher_at.group(4);
                if (hour_ago != null) {
                    cal.add(Calendar.HOUR, -Integer.parseInt(hour_ago));
                } else if (date_ago != null) {
                    cal.add(Calendar.DATE, -Integer.parseInt(date_ago));
                } else if (date_time != null) {
                    final String hour = matcher_at.group(5);
                    final String minute = matcher_at.group(6);
                    final String hour_only = matcher_at.group(7);
                    if (hour != null && minute != null) {
                        cal.set(Calendar.HOUR, Integer.parseInt(hour));
                        cal.set(Calendar.MINUTE, Integer.parseInt(minute));
                    } else if (hour_only != null) {
                        cal.set(Calendar.HOUR, Integer.parseInt(hour_only));
                        cal.set(Calendar.MINUTE, 0);
                    } else {
                        return null;
                    }

                    final String on_date_ago = matcher_at.group(9);
                    final String on_yesterday = matcher_at.group(10);
                    final String on_the_day_before_yesterday = matcher_at.group(11);
                    if (on_date_ago != null) {
                        cal.add(Calendar.DATE, -Integer.parseInt(on_date_ago));
                    } else if (on_yesterday != null) {
                        cal.add(Calendar.DATE, -1);
                    } else if (on_the_day_before_yesterday != null) {
                        cal.add(Calendar.DATE, -2);
                    }
                }
            }
            return cal.getTime();
        }
        return null;
    }

    private enum Keys {
        BUY_SELL,
        PRICE,
        LOCATION,
        TIME,
        WITH
    }
}
