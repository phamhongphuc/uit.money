package ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
    private int focusColor;

    public InputText(Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeView();
        initializeFocus();
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputText);
        font = FONTs.get(typedArray.getInt(R.styleable.InputText__font, 2));
        autoSize = typedArray.getBoolean(R.styleable.InputText__autoSize, false);
        focusColor = typedArray.getColor(R.styleable.InputText__focusColor, Color.TRANSPARENT);

        typedArray.recycle();
    }

    private void initializeView() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
    }

    private void initializeFocus() {
        if (focusColor == Color.TRANSPARENT) return;
        setOnFocusChangeListener((v, hasFocus) -> {
            int colorFrom = hasFocus ? Color.TRANSPARENT : focusColor;
            int colorTo = !hasFocus ? Color.TRANSPARENT : focusColor;

            AnimatorSet set;
            set = new AnimatorSet();
            set.setDuration(150);
            set.play(ObjectAnimator.ofArgb(this, "backgroundColor", colorFrom, colorTo));
            if (!set.isRunning()) set.start();
        });
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
        final float dp = getResources().getDisplayMetrics().density;
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int paddingSize = (int) (height * 0.75f / dp);

        if (autoSize) setTextSize(height * 0.35f / dp);
        setPadding(
                firstOrSecond(getPaddingLeft(), paddingSize),
                getPaddingTop(),
                firstOrSecond(getPaddingRight(), paddingSize),
                getPaddingBottom()
        );
    }

    private static int firstOrSecond(int first, int second) {
        return first != 0 ? first : second;
    }
}
