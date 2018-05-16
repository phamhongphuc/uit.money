package ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.text.DecimalFormat;

public class InputMoney extends InputText {
    public InputMoney(Context context) {
        super(context);
        initialize();
    }

    public InputMoney(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public InputMoney(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        addTextChangedListener(new MoneyTextWatcher(this));
    }

    class MoneyTextWatcher implements TextWatcher {
        private InputText inputText;

        MoneyTextWatcher(InputText inputText) {
            this.inputText = inputText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            inputText.removeTextChangedListener(this);

            final DecimalFormat format = new DecimalFormat("###,###,### đ");
            String string = editable.toString().replaceAll("[^0-9]", "");
            if (string.equals("")) string = "0";
            final String number = format.format(Long.parseLong(string));
            inputText.setText(number);
            inputText.setSelection(number.length() - 2);

            inputText.addTextChangedListener(this);
        }
    }
}

