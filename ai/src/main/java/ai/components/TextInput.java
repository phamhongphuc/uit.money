package ai.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import ai.ai.lib.R;

public class TextInput extends AppCompatEditText {
    private static final String REGULAR = "fonts/segoe regular.ttf";
    private static final String LIGHT = "fonts/segoe light.ttf";
    private static final String BOLD = "fonts/segoe bold.ttf";
    private static final Map<Integer, String> FONTs = ImmutableMap.<Integer, String>builder()
            .put(0, REGULAR)
            .put(1, LIGHT)
            .put(2, BOLD)
            .build();

    private boolean autoSize;
    private String font;

    public TextInput(Context context) {
        super(context);
        Initialize(context, null);
    }

    public TextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initialize(context, attrs);
    }

    public TextInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialize(context, attrs);
    }

    private void Initialize(Context context, AttributeSet attrs) {
        InitializeAttr(context, attrs);
        InitializeView(context);
    }

    private void InitializeView(Context context) {
        setTypeface(
                Typeface.createFromAsset(getContext().getAssets(), font)
        );
        setTextColor(Color.parseColor("#08c"));
        setBackgroundColor(Color.TRANSPARENT);
        setForeground(context
                .obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless})
                .getDrawable(0)
        );
    }

    private void InitializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextInput);
        autoSize = typedArray.getBoolean(R.styleable.TextInput__autoSize, true);
        font = FONTs.get(typedArray.getInt(R.styleable.Button__font, 0));

        typedArray.recycle();
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
                    (int) (size * 0.5f / dp), 0,
                    (int) (size * 0.5f / dp), 0
            );
        }
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            clearFocus();
        }
    }
}
