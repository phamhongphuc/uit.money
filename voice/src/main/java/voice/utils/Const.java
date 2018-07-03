package voice.utils;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static voice.utils.Const.Key.*;

public class Const {
    static final Map<Key, List<String>> ALL_KEYS = ImmutableMap.<Key, List<String>>builder()
            .put(_BUY_SELL, asList("mua", "bán", "sắm"))

            .put(_PICK, asList("nhặt được", "lượm được"))
            .put(_LOST, asList("làm mất", "làm rơi", "đánh rơi"))

            .put(_HAVE_BREAKFAST, of("ăn sáng"))
            .put(_HAVE_LUNCH, of("ăn trưa"))
            .put(_HAVE_DINNER, of("ăn tối"))

            .put(_SALARY, asList("nhận lương", "lãnh lương"))
            .put(_INTERNET_BILL, asList("thanh toán hóa đơn mạng", "thanh toán hóa đơn tiền mạng", "thanh toán hóa đơn internet"))
            .put(_ELECTRIC_BILL, asList("thanh toán hóa đơn điện", "thanh toán hóa đơn tiền điện"))
            .put(_WATTER_BILL, asList("thanh toán hóa đơn nước", "thanh toán hóa đơn tiền nước"))

            .put(_PRICE, asList("hết", "tốn", "với giá", "mất", "giá"))
            .put(_LOCATION, asList("tại", "ở", "ngoài", "ở ngoài"))
            .put(_TIME, asList("lúc", "vào lúc", "hồi", "ngày"))
            .put(_WITH, asList("đi cùng", "cùng với", "cùng", "đi với"))

            .build();

    public enum Key {
        _BUY_SELL,
        _PICK,
        _LOST,
        _HAVE_BREAKFAST,
        _HAVE_LUNCH,
        _HAVE_DINNER,

        _SALARY,
        _INTERNET_BILL,
        _ELECTRIC_BILL,
        _WATTER_BILL,

        _PRICE,
        _LOCATION,
        _TIME,
        _WITH
    }
}
