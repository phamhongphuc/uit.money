package ui;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;

import ui.ui.R;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setKeyListener(DigitsKeyListener.getInstance(false, true));
        addTextChangedListener(new MoneyTextWatcher());
    }

    // TODO loop button when holding
    class MoneyTextWatcher implements TextWatcher {
        private static final String TAG = "MoneyTextWatcher";
        boolean isEditing;

        MoneyTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG, "onTextChanged: " + count);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (isEditing) return;
            isEditing = true;

            try {
                final DecimalFormat format = new DecimalFormat("###,###,###");
                String string = editable.toString().replaceAll("[^0-9]", "");
                if (string.equals("")) string = "0";
                if (string.length() > 12) {
                    string = "999999999999";
                    Toast.makeText(getApplicationContext(), R.string.so_much_money, Toast.LENGTH_SHORT).show();
                }
                final String number = format.format(Long.parseLong(string));

                InputFilter[] filters = editable.getFilters();
                editable.setFilters(new InputFilter[]{});
                editable.clear();
                editable.insert(0, number);
                editable.setFilters(filters);
            } catch (Exception ignored) {

            }

            isEditing = false;
        }
    }
}

