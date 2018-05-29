package ui;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

public class InputNumber extends InputText {
    public InputNumber(Context context) {
        super(context);
        initialize();
    }

    public InputNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public InputNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setKeyListener(DigitsKeyListener.getInstance(false, true));
        setFilters(new InputFilter[]{new InputFilterMinMax(1, 999)});
    }

    class InputFilterMinMax implements InputFilter {
        private int min, max;

        InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int destStart, int destEnd) {
            try {
                final String d = dest.toString();
                final int l = d.length();
                String newVal = d.substring(0, destStart) + d.substring(destEnd, l);
                newVal = newVal.substring(0, destStart) + source.toString() + newVal.substring(destStart, newVal.length());
                int input = Integer.parseInt(newVal);

                if (isInRange(min, max, input)) return null;
            } catch (NumberFormatException ignored) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}

