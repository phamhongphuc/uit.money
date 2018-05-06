package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ui.ui.R;

public class Shadow extends View {
    private final Paint paint = new Paint();
    private int shadowColor;
    private int shadowSize;
    private int radius;
    private int backgroundColor;
    private int dx;
    private int dy;

    public Shadow(Context context) {
        super(context);
        initialize(context, null);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializePaint();
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Shadow);

        radius = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Shadow__radius, 0));
        shadowSize = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Shadow__shadowSize, 0));
        dx = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Shadow__dx, 0));
        dy = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Shadow__dy, 0));
        shadowColor = typedArray.getColor(R.styleable.Shadow__shadowColor, Color.TRANSPARENT);
        backgroundColor = typedArray.getColor(R.styleable.Shadow__backgroundColor, Color.TRANSPARENT);

        typedArray.recycle();
    }

    private void initializePaint() {
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowSize , dx, dy, shadowColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius, paint);
    }

    //region setter[shadowColor, shadowRadius]
    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        initializePaint();
    }

    public void setShadowSize(int shadowRadius) {
        this.shadowSize = shadowRadius;
        initializePaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        initializePaint();
    }

    public void setBackground(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        initializePaint();
    }
    //endregion
}