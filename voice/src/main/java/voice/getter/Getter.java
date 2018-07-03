package voice.getter;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import voice.utils.Utils;

import static voice.utils.Utils.getMatcher;

public class Getter {

    public static Date getTime(String input, String stop) {
        final Matcher matcher_time = getMatcher(
                input, String.format("((vào lúc|vào|lúc) (([^\\r\\n](?!(%s)))+)|ngày (hôm qua)|(hôm kia))(?!(%s))[ \\.]", stop, stop)
        );
        if (matcher_time.find()) {
            final String yesterday = matcher_time.group(6);
            final String the_day_before_yesterday = matcher_time.group(7);
            final String at = matcher_time.group(3);

            final Calendar cal = Calendar.getInstance();
            if (yesterday != null) {
                cal.add(Calendar.DATE, -1);
            } else if (the_day_before_yesterday != null) {
                cal.add(Calendar.DATE, -2);
            } else if (at != null) {
                final Matcher matcher_at = getMatcher(
                        at, "((\\d+|một|hai|ba|bốn|năm) giờ trước|(\\d+|một|hai|ba|bốn|năm) ngày trước|((\\d+):(\\d+)|(\\d+|một|hai|ba|bốn|năm) giờ)( (\\d+|một|hai|ba|bốn|năm) ngày trước|( ngày hôm qua)|( ngày hôm kia))?)"
                );
                if (!matcher_at.find()) return null;

                final String hour_ago = matcher_at.group(2);
                final String date_ago = matcher_at.group(3);
                final String date_time = matcher_at.group(4);
                if (hour_ago != null) {
                    cal.add(Calendar.HOUR, -Utils.getInt(hour_ago));
                } else if (date_ago != null) {
                    cal.add(Calendar.DATE, -Utils.getInt(date_ago));
                } else if (date_time != null) {
                    final String hour = matcher_at.group(5);
                    final String minute = matcher_at.group(6);
                    final String hour_only = matcher_at.group(7);
                    if (hour != null && minute != null) {
                        cal.set(Calendar.HOUR_OF_DAY, Utils.getInt(hour));
                        cal.set(Calendar.MINUTE, Utils.getInt(minute));
                    } else if (hour_only != null) {
                        cal.set(Calendar.HOUR_OF_DAY, Utils.getInt(hour_only));
                        cal.set(Calendar.MINUTE, 0);
                    } else {
                        return null;
                    }

                    final String on_date_ago = matcher_at.group(9);
                    final String on_yesterday = matcher_at.group(10);
                    final String on_the_day_before_yesterday = matcher_at.group(11);
                    if (on_date_ago != null) {
                        cal.add(Calendar.DATE, -Utils.getInt(on_date_ago));
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

    public static long getMoney(String input) {
        final String number = input
                .replaceAll("nghìn", "000")
                .replaceAll("triệu", "000000")
                .replaceAll("tỷ", "000000000")
                .replaceAll("[^0-9]", "");
        if("".equals(number)) return 0;
        return Math.abs(Long.parseLong(number));
    }
}
