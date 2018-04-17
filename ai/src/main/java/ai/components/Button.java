package ai.components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

import ai.ai.lib.R;

public class Button extends LinearLayoutCompat {
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
            .put(0, REGULAR)
            .put(1, LIGHT)
            .put(2, BOLD)
            .build();

    private static final float SMALL = 0.35f;
    private static final float LARGE = 0.6f;
    private static final float MEDIUM = 0.5f;
    private static final Map<Integer, Float> SIZEs = ImmutableMap.<Integer, Float>builder()
            .put(0, SMALL)
            .put(1, LARGE)
            .put(2, MEDIUM)
            .build();

    private String text;
    private String icon;
    private boolean active;
    private int backgroundActive;
    private int background;
    private int foreground;
    private AppCompatTextView textView;
    private AppCompatTextView iconView;
    private int textAlign;
    private boolean textPaddingLeft;
    private boolean hasRipple;
    private String font;
    private Float fontSize;

    public Button(Context context) {
        super(context, null, R.attr.ButtonStyle);
        Initialize(context, null);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.ButtonStyle);
        Initialize(context, attrs);
    }

    private void Initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        InitializeAttr(context, attrs);
        InitializeView(context);
        InitializeRipple();
    }

    private void InitializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button);
        icon = (String) typedArray.getText(R.styleable.Button__icon);
        text = (String) typedArray.getText(R.styleable.Button__text);
        textAlign = ALIGN.get(typedArray.getInt(R.styleable.Button__textAlign, CENTER)) | Gravity.CENTER_VERTICAL;

        active = typedArray.getBoolean(R.styleable.Button__active, false);
        background = typedArray.getColor(R.styleable.Button__background, Color.TRANSPARENT);
        foreground = typedArray.getColor(R.styleable.Button__foreground, Color.parseColor("#3498db"));
        backgroundActive = typedArray.getColor(R.styleable.Button__backgroundActive, Color.parseColor("#333498db"));

        hasRipple = typedArray.getBoolean(R.styleable.Button__hasRipple, true);

        textPaddingLeft = typedArray.getBoolean(R.styleable.Button__textPaddingLeft, true);
        font = FONTs.get(typedArray.getInt(R.styleable.Button__font, 0));
        fontSize = SIZEs.get(typedArray.getInt(R.styleable.Button__fontSize, 0));

        typedArray.recycle();
    }

    private void InitializeView(Context context) {
        int currentColor = !active ? background : backgroundActive;
        InitializeIcon(context, currentColor);
        InitializeText(context, currentColor);
        requestLayout();
    }

    private void InitializeText(Context context, int currentColor) {
        if (text != null && !Objects.equals(text, "")) {
            if (textView == null) {
                textView = new AppCompatTextView(context);
                addView(textView);
            }
            textView.setText(text);
            textView.setTypeface(
                    Typeface.createFromAsset(getContext().getAssets(), font)
            );
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            params.weight = 1;
            textView.setLayoutParams(params);
            textView.setTextColor(foreground);
            textView.setSingleLine(true);
            textView.setBackgroundColor(currentColor);
            textView.setGravity(textAlign);
            textView.setIncludeFontPadding(false);
        } else if (textView != null) {
            removeView(textView);
        }
    }

    private void InitializeIcon(Context context, int currentColor) {
        if (icon != null && !Objects.equals(icon, "")) {
            if (iconView == null) {
                iconView = new AppCompatTextView(context);
                addView(iconView);
            }
            iconView.setText(icon);
            iconView.setTypeface(
                    Typeface.createFromAsset(getContext().getAssets(), ICON)
            );
            iconView.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            ));
            iconView.setIncludeFontPadding(false);
            iconView.setGravity(Gravity.CENTER);
            iconView.setTextColor(foreground);
            iconView.setBackgroundColor(currentColor);
        } else if (iconView != null) {
            removeView(iconView);
        }
    }

    private void InitializeRipple() {
        setForeground(!active && hasRipple ?
                getContext()
                        .obtainStyledAttributes(new int[]{R.attr.selectableItemBackground})
                        .getDrawable(0) :
                new ColorDrawable(Color.TRANSPARENT)
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float dp = getResources().getDisplayMetrics().density;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMinimumHeight(size);

        if (iconView != null) {
            iconView.setTextSize(size * (fontSize + 0.05f) / dp);
            iconView.setWidth(size);
        }
        if (textView != null) {
            textView.setTextSize(size * fontSize / dp);
            textView.setPadding(
                    (iconView == null && textPaddingLeft)
                            ? (int) (size * 0.5f / dp) : 0,
                    0,
                    (int) (size * 0.5f / dp),
                    0
            );
        }
    }

    public void set_text(String text) {
        this.text = text;
        InitializeView(getContext());
    }

    public void set_icon(String icon) {
        this.icon = icon;
        InitializeView(getContext());
    }

    public void toggleActive() {
        active = !active;
        SetActiveAnimation();
    }

    public void SetActiveAnimation(boolean active) {
        this.active = active;
        SetActiveAnimation();
    }

    public void SetActiveAnimation() {
        InitializeRipple();
        int colorFrom = active ? background : backgroundActive;
        int colorTo = !active ? background : backgroundActive;

        AnimatorSet set;
        set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofArgb(iconView, "backgroundColor", colorFrom, colorTo),
                ObjectAnimator.ofArgb(textView, "backgroundColor", colorFrom, colorTo)
        );
        set.setDuration(150);
        if (!set.isRunning()) set.start();

        invalidate();
    }
}
