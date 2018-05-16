package ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import java.util.Arrays;

import ui.ui.R;

import static ui.utils.Const.FONTs;

public class InputText extends AppCompatEditText {
    private boolean autoSize;
    private String font;
    private int rippleColor;

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
        rippleColor = typedArray.getColor(R.styleable.InputText__rippleColor, Color.TRANSPARENT);

        typedArray.recycle();
    }

    private void initializeView(Context context) {
        setTypeface(
                Typeface.createFromAsset(getContext().getAssets(), font)
        );
        setForeground(getRippleDrawable());
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

    @NonNull
    private RippleDrawable getRippleDrawable() {
        float[] outer = new float[8];
        Arrays.fill(outer, 0);
        RoundRectShape roundRectShape = new RoundRectShape(
                outer,
                null,
                null
        );
        return new RippleDrawable(
                ColorStateList.valueOf(rippleColor),
                null,
                new ShapeDrawable(roundRectShape)
        );
    }
}
