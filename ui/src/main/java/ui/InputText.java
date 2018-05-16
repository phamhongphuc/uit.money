package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import ui.ui.R;

import static ui.utils.Const.FONTs;

public class InputText extends AppCompatEditText {
    private boolean autoSize;
    private String font;

    public InputText(Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeView(context);
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputText);
        autoSize = typedArray.getBoolean(R.styleable.InputText__autoSize, false);
        font = FONTs.get(typedArray.getInt(R.styleable.InputText__font, 2));

        typedArray.recycle();
    }

    private void initializeView(Context context) {
        setTypeface(
                Typeface.createFromAsset(getContext().getAssets(), font)
        );
        setForeground(context
                .obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless})
                .getDrawable(0)
        );
    }

    public InputText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public InputText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            clearFocus();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float dp = getResources().getDisplayMetrics().density;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);

        if (autoSize) {
            setTextSize(size * 0.35f / dp);
            setPadding(
                    (int) (size * 0.75f / dp), 0,
                    (int) (size * 0.75f / dp), 0
            );
        }
    }
}
