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
import android.widget.FrameLayout;

import ui.ui.R;
import ui.utils.ShadowView;

import static ui.utils.Calc.setShadow;

public class FrameShadow extends FrameLayout implements ShadowView {
    private final Paint paint = new Paint();
    private int radius;
    private int dx;
    private int dy;
    private int shadowSize;
    private int shadowColor;
    private boolean extendTop;
    private boolean extendLeft;
    private boolean extendRight;
    private boolean extendBottom;

    public FrameShadow(Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeView();
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FrameShadow);

        radius = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__radius, 0));
        dx = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__dx, 0));
        dy = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__dy, 0));
        shadowSize = MeasureSpec.getSize(typedArray.getDimensionPixelSize(R.styleable.FrameShadow__shadowSize, 0));
        shadowColor = typedArray.getColor(R.styleable.FrameShadow__shadowColor, Color.TRANSPARENT);
        extendTop = typedArray.getBoolean(R.styleable.FrameShadow__extendTop, true);
        extendLeft = typedArray.getBoolean(R.styleable.FrameShadow__extendLeft, true);
        extendRight = typedArray.getBoolean(R.styleable.FrameShadow__extendRight, true);
        extendBottom = typedArray.getBoolean(R.styleable.FrameShadow__extendBottom, true);

        typedArray.recycle();
    }

    private void initializeView() {
        setShadow(this, paint);

        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        setPadding(
                extendLeft ? shadowSize : 0,
                extendTop ? shadowSize : 0,
                extendRight ? shadowSize : 0,
                extendBottom ? shadowSize : 0
        );
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
    public int getShadowSize() {
        return shadowSize;
    }

    @Override
    public int getDx() {
        return dx;
    }

    @Override
    public int getDy() {
        return dy;
    }

    @Override
    public int getShadowColor() {
        return shadowColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(
                extendLeft ? shadowSize : 0,
                extendTop ? shadowSize : 0,
                getWidth() - (extendRight ? shadowSize : 0),
                getHeight() - (extendBottom ? shadowSize : 0),
                radius, radius, paint
        );
    }

    public View getView() {
        return this;
    }
}
