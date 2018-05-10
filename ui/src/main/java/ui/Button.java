package ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import ui.ui.R;

public class Button extends FrameLayout {
    private static final String TAG = "Ai.Button";

    private static final int LEFT = 0;
    private static final int CENTER = 1;
    private static final int RIGHT = 2;
    private static final Map<Integer, Integer> ALIGN = ImmutableMap.<Integer, Integer>builder()
            .put(LEFT, Gravity.START)
            .put(CENTER, Gravity.CENTER_HORIZONTAL)
            .put(RIGHT, Gravity.END)
            .build();

    private static final String ICON = "fonts/aicon.ttf";
    private static final String REGULAR = "fonts/segoe regular.ttf";
    private static final String LIGHT = "fonts/segoe light.ttf";
    private static final String BOLD = "fonts/segoe bold.ttf";
    private static final Map<Integer, String> FONTs = ImmutableMap.<Integer, String>builder()
            .put(1, REGULAR)
            .put(2, LIGHT)
            .put(3, BOLD)
            .build();

    private static final float SMALL = 0.35f;
    private static final float LARGE = 0.6f;
    private static final float MEDIUM = 0.45f;
    private static final Map<Integer, Float> FONT_SIZEs = ImmutableMap.<Integer, Float>builder()
            .put(0, SMALL)
            .put(1, LARGE)
            .put(2, MEDIUM)
            .build();

    private static final int RADIUS_NONE = 0;
    private static final int RADIUS_ICON = 1;
    private static final int RADIUS_TEXT = 2;

    private String text;
    private String icon;

    private int background;
    private int foreground;
    private int backgroundIcon;
    private int backgroundText;
    private int textAlign;

    private int radius;
    private int shadowColor;
    private int rippleColor;

    private AppCompatTextView textView;
    private AppCompatTextView iconView;
    private LinearLayoutCompat linearLayoutCompat;

    private boolean textPaddingLeft;
    private int childRadius;
    private String font;
    private Float fontSize;
    private int shadowSize;

    public Button(Context context) {
        super(context, null);
        initialize(context, null);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        setClipChildren(false);
        setClickable(true);

        initializeAttrs(context, attrs);
        initializeShadow(context);
        initializeLinearLayoutCompat(context);
        initializeIcon(context);
        initializeText(context);
    }

    private void initializeAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button);
        icon = (String) typedArray.getText(R.styleable.Button__icon);
        text = (String) typedArray.getText(R.styleable.Button__text);
        textAlign = ALIGN.get(typedArray.getInt(R.styleable.Button__textAlign, CENTER)) | Gravity.CENTER_VERTICAL;

        background = typedArray.getColor(R.styleable.Button__background, Color.WHITE);
        foreground = typedArray.getColor(R.styleable.Button__foreground, context.getColor(R.color.foreground));

        backgroundIcon = typedArray.getColor(R.styleable.Button__backgroundIcon, Color.TRANSPARENT);
        backgroundText = typedArray.getColor(R.styleable.Button__backgroundText, Color.TRANSPARENT);

        textPaddingLeft = typedArray.getBoolean(R.styleable.Button__textPaddingLeft, true);
        childRadius = typedArray.getInt(R.styleable.Button__childRadius, RADIUS_NONE);
        font = FONTs.get(typedArray.getInt(R.styleable.Button__font, 0));
        fontSize = FONT_SIZEs.get(typedArray.getInt(R.styleable.Button__fontSize, 0));

        radius = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Button__radius, 0));
        shadowSize = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Button__shadowSize, 0));
        shadowColor = typedArray.getColor(R.styleable.Button__shadowColor, Color.TRANSPARENT);
        rippleColor = typedArray.getColor(R.styleable.Button__rippleColor, Color.WHITE);

        typedArray.recycle();
    }

    private void initializeShadow(Context context) {
        Shadow shadow = new Shadow(context);
        shadow.setForeground(getRippleDrawable());
        shadow.setClickable(true);
        shadow.setShadowColor(shadowColor);
        shadow.setBackground(background);
        shadow.setShadowSize(shadowSize);
        shadow.setRadius(radius);
        shadow.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
        shadow.setOnClickListener(v -> callOnClick());
        addView(shadow);
    }

    private void initializeLinearLayoutCompat(Context context) {
        linearLayoutCompat = new LinearLayoutCompat(context);
        linearLayoutCompat.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
        linearLayoutCompat.setClipChildren(true);
        linearLayoutCompat.setClipToOutline(true);
        addView(linearLayoutCompat);
    }

    private void initializeIcon(Context context) {
        if (icon != null && !Objects.equals(icon, "")) {
            if (iconView == null) {
                iconView = new AppCompatTextView(context);
                linearLayoutCompat.addView(iconView);
            }
            iconView.setText(icon);
            iconView.setTypeface(
                    Typeface.createFromAsset(getContext().getAssets(), ICON)
            );
            iconView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
            ));
            iconView.setIncludeFontPadding(false);
            iconView.setGravity(Gravity.CENTER);
            iconView.setTextColor(foreground);
            iconView.setBackgroundDrawable(getGradientDrawable(backgroundIcon, radius, childRadius == RADIUS_ICON ? radius : 0));
        } else if (iconView != null) {
            linearLayoutCompat.removeView(iconView);
        }
    }

    private void initializeText(Context context) {
        if (text != null && !Objects.equals(text, "")) {
            if (textView == null) {
                textView = new AppCompatTextView(context);
                linearLayoutCompat.addView(textView);
            }
            textView.setText(text);
            textView.setSingleLine(true);
            textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
            textView.setGravity(textAlign);
            textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT, 1
            ));
            textView.setIncludeFontPadding(false);
            textView.setTextColor(foreground);
            textView.setBackgroundDrawable(getGradientDrawable(backgroundText, childRadius == RADIUS_TEXT ? radius : 0, radius));
        } else if (textView != null) {
            linearLayoutCompat.removeView(textView);
        }
    }

    @NonNull
    private RippleDrawable getRippleDrawable() {
        float[] outer = new float[8];
        Arrays.fill(outer, radius);
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

    @NonNull
    private GradientDrawable getGradientDrawable(@ColorInt int color, float left, float right) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadii(new float[]{
                left, left, right, right, right, right, left, left
        });
        return drawable;
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float dp = getResources().getDisplayMetrics().density;
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int size = Math.min(width, height);

        linearLayoutCompat.setMinimumHeight(size);
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

    public void set_text(String text) {
        this.text = text;
        initializeText(getContext());
    }

    public void set_icon(String icon) {
        this.icon = icon;
        initializeIcon(getContext());
    }
}
