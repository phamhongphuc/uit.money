package ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ui.ui.R;

public class Circle extends View {
    private static final String TAG = "Circle";
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int minSize;
    private int backgroundColor;
    private float ratio;
    private int width;
    private int height;
    private AnimatorSet animatorSet = new AnimatorSet();

    public Circle(Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        initializeAttrs(context, attrs);
        initializePaint();
    }

    private void initializePaint() {
        paint.setColor(backgroundColor);
    }

    private void initializeAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Circle);

        minSize = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Circle__minSize, 0));
        ratio = typedArray.getFloat(R.styleable.Circle__ratio, 0);
        backgroundColor = typedArray.getColor(R.styleable.Circle__backgroundColor, Color.BLUE);

        typedArray.recycle();
    }

    public Circle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int size = Math.min(width, height);
        final int radius = (int) (minSize + (size - minSize) * ratio) / 2;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
    }

    public void set_ratio(float ratio) {
        Log.i(TAG, "set_ratio: " + ratio);
        if (animatorSet.isRunning()) animatorSet.cancel();
        animatorSet.playSequentially(
                ObjectAnimator.ofFloat(this, "__ratio", this.ratio, ratio).setDuration(50),
                ObjectAnimator.ofFloat(this, "__ratio", ratio, 0).setDuration(600)
        );
        animatorSet.start();
    }

    public void set__ratio(float ratio) {
        this.ratio = ratio;
        Log.i(TAG, "set__ratio: " + ratio + " - " + this.ratio);
        invalidate();
    }
}
