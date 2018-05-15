package ui;

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

import static ui.utils.Const.BOLD;
import static ui.utils.Const.LIGHT;
import static ui.utils.Const.NONE;
import static ui.utils.Const.REGULAR;

public class Text extends AppCompatTextView {
    private static final String TAG = "Text";

    private static final Map<Integer, String> FONTs = ImmutableMap.<Integer, String>builder()
            .put(0, NONE)
            .put(1, REGULAR)
            .put(2, LIGHT)
            .put(3, BOLD)
            .build();

    private String font;
    private boolean autoSize;

    public Text(Context context) {
        super(context, null);
        initialize(context, null);
    }

    public Text(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Text(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeTypeface(context);
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Text);

        font = FONTs.get(typedArray.getInt(R.styleable.Text__font, 1));
        autoSize = typedArray.getBoolean(R.styleable.Text__autoSize, false);

        typedArray.recycle();
    }

    private void initializeTypeface(Context context) {
        if (!font.equals(NONE)) {
            setIncludeFontPadding(false);
            setTypeface(
                    Typeface.createFromAsset(context.getAssets(), font)
            );
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float dp = getResources().getDisplayMetrics().density;
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (autoSize) setTextSize(height * 0.35f / dp);
    }

    public void set_font(String font) {
        this.font = font;
        initializeTypeface(getContext());
    }
}
