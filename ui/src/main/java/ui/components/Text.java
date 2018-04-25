package ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import ui.ui.R;

public class Text extends AppCompatTextView {
    private static final String REGULAR = "fonts/segoe regular.ttf";
    private static final String LIGHT = "fonts/segoe light.ttf";
    private static final String BOLD = "fonts/segoe bold.ttf";
    private static final Map<Integer, String> FONTs = ImmutableMap.<Integer, String>builder()
            .put(0, REGULAR)
            .put(1, LIGHT)
            .put(2, BOLD)
            .build();

    private String font;

    public Text(Context context) {
        super(context, null);
        Initialize(context, null);
    }

    public Text(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initialize(context, attrs);
    }

    public Text(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialize(context, attrs);
    }

    private void Initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        InitializeAttr(context, attrs);
        InitializeTypeface(context);
    }

    private void InitializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Text);
        font = FONTs.get(typedArray.getInt(R.styleable.Text__font, 0));

        typedArray.recycle();
    }

    private void InitializeTypeface(Context context) {
        setIncludeFontPadding(false);
        setTypeface(
                Typeface.createFromAsset(context.getAssets(), font)
        );
    }
}
