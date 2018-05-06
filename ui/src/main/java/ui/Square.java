package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import ui.ui.R;

public class Square extends FrameLayout {
    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;
    private int by;

    public Square(@NonNull Context context) {
        super(context);
    }

    public Square(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Square(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Square);

        by = typedArray.getInt(R.styleable.Square__by, 0);
//        autoSize = typedArray.getBoolean(R.styleable.Text__autoSize, false);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float dp = getResources().getDisplayMetrics().density;
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (by){
            case WIDTH:
                setMinimumHeight(width);
                break;
            case HEIGHT:
                setMinimumWidth(height);
                break;
        }
    }
}
