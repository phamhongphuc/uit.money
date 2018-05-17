package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import ui.ui.R;

public class FrameShadow extends FrameLayout {
    private final Paint paint = new Paint();
    private int radius;
    private int shadowSize;
    private int dx;
    private int dy;
    private int shadowColor;
    private int backgroundColor;

    public FrameShadow(Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeShadow();
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FrameShadow);

        radius = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__radius, 0));
        shadowSize = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__shadowSize, 0));
        dx = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__dx, 0));
        dy = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__dy, 0));
        shadowColor = typedArray.getColor(R.styleable.FrameShadow__shadowColor, Color.TRANSPARENT);

        typedArray.recycle();
    }

    private void initializeShadow() {
        final Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            backgroundColor = ((ColorDrawable) background).getColor();
            setBackgroundColor(Color.TRANSPARENT);
        } else {
            backgroundColor = Color.WHITE;
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowSize, dx, dy, shadowColor);
    }

    public FrameShadow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public FrameShadow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius, paint);
    }
}
