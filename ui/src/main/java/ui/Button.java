package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import java.util.Objects;

import ui.ui.R;
import ui.utils.Calc;
import ui.utils.ShadowView;

import static ui.utils.Calc.getRippleDrawable;
import static ui.utils.Calc.setShadow;
import static ui.utils.Const.ALIGN;
import static ui.utils.Const.CENTER;
import static ui.utils.Const.FONT_SIZEs;
import static ui.utils.Const.FONTs;
import static ui.utils.Const.ICON;
import static ui.utils.Const.RADIUS_ICON;
import static ui.utils.Const.RADIUS_NONE;
import static ui.utils.Const.RADIUS_TEXT;

public class Button extends LinearLayoutCompat implements ShadowView {
    private String text;
    private String icon;

    private int foreground;
    private int backgroundIcon;
    private int backgroundText;
    private int textAlign;

    private int radius;
    private int shadowColor;
    private int rippleColor;

    private AppCompatTextView textView;
    private AppCompatTextView iconView;

    private String font;
    private Float fontSize;
    private boolean textPaddingLeft;
    private int childRadius;
    private int shadowSize;
    private int paddingLeft;
    private int paddingRight;
    private Paint paint = new Paint();

    public Button(Context context) {
        super(context, null);
        initialize(context, null);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        setClickable(true);

        initializeAttrs(context, attrs);
        initializeView();
        initializeIcon(context);
        initializeText(context);
    }

    private void initializeAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button);
        icon = (String) typedArray.getText(R.styleable.Button__icon);
        text = (String) typedArray.getText(R.styleable.Button__text);
        textAlign = ALIGN.get(typedArray.getInt(R.styleable.Button__textAlign, CENTER)) | Gravity.CENTER_VERTICAL;

        foreground = typedArray.getColor(R.styleable.Button__foreground, context.getColor(R.color.foreground));

        backgroundIcon = typedArray.getColor(R.styleable.Button__backgroundIcon, Color.TRANSPARENT);
        backgroundText = typedArray.getColor(R.styleable.Button__backgroundText, Color.TRANSPARENT);

        textPaddingLeft = typedArray.getBoolean(R.styleable.Button__textPaddingLeft, true);
        childRadius = typedArray.getInt(R.styleable.Button__childRadius, RADIUS_NONE);
        font = FONTs.get(typedArray.getInt(R.styleable.Button__font, 2));
        fontSize = FONT_SIZEs.get(typedArray.getInt(R.styleable.Button__fontSize, 0));

        radius = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Button__radius, 0));
        shadowSize = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Button__shadowSize, 0));
        shadowColor = typedArray.getColor(R.styleable.Button__shadowColor, Color.TRANSPARENT);
        rippleColor = typedArray.getColor(R.styleable.Button__rippleColor, Color.WHITE);

        paddingLeft = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Button__paddingLeft, 0));
        paddingRight = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Button__paddingRight, 0));

        typedArray.recycle();
    }

    private void initializeView() {
        setClipChildren(false);
        setClipToOutline(false);
        setShadow(this, paint);
        setForeground(getRippleDrawable(rippleColor, radius, radius));
        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setPadding(paddingLeft + shadowSize, shadowSize, paddingRight + shadowSize, shadowSize);
    }

    private void initializeIcon(Context context) {
        if (icon != null && !Objects.equals(icon, "")) {
            if (iconView == null) {
                iconView = new AppCompatTextView(context);
                addView(iconView);
            }
            iconView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            iconView.setText(icon);
            iconView.setTypeface(
                    Typeface.createFromAsset(context.getAssets(), ICON)
            );
            iconView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
            ));
            iconView.setIncludeFontPadding(false);
            iconView.setGravity(Gravity.CENTER);
            iconView.setTextColor(foreground);
            iconView.setBackgroundDrawable(Calc.getPaintDrawable(backgroundIcon, radius, childRadius == RADIUS_ICON ? radius : 0));
        } else if (iconView != null) {
            removeView(iconView);
        }
    }

    private void initializeText(Context context) {
        if (text != null && !Objects.equals(text, "")) {
            if (textView == null) {
                textView = new AppCompatTextView(context);
                addView(textView);
            }
            textView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            textView.setText(text);
            textView.setSingleLine(true);
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), font));
            textView.setGravity(textAlign);
            textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT, 1
            ));
            textView.setIncludeFontPadding(false);
            textView.setTextColor(foreground);
            textView.setBackgroundDrawable(Calc.getPaintDrawable(backgroundText, childRadius == RADIUS_TEXT ? radius : 0, radius));
        } else if (textView != null) {
            removeView(textView);
        }
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public void set_text(String text) {
        this.text = text;
        initializeText(getContext());
    }

    public void set_icon(String icon) {
        this.icon = icon;
        initializeIcon(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(
                shadowSize, shadowSize, getWidth() - shadowSize, getHeight() - shadowSize,
                radius, radius, paint
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float dp = getResources().getDisplayMetrics().density;
        final int size = MeasureSpec.getSize(heightMeasureSpec) - 2 * shadowSize;

        setMinimumHeight(size);
        if (iconView != null) {
            iconView.setTextSize(size * (fontSize + 0.05f) / dp);
            iconView.setWidth(size);
        }
        if (textView != null) {
            final int paddingSize = (int) (0.75f * size / dp);
            textView.setTextSize(size * fontSize / dp);
            textView.setPadding(
                    (icon == null || textPaddingLeft) ? paddingSize : 0,
                    0,
                    paddingSize,
                    0
            );
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getShadowSize() {
        return shadowSize;
    }

    @Override
    public int getDx() {
        return 0;
    }

    @Override
    public int getDy() {
        return 0;
    }

    @Override
    public int getShadowColor() {
        return shadowColor;
    }
}
