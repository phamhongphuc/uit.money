package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ui.ui.R;

import static ui.utils.Calc.getOuterRadii;
import static ui.utils.Calc.getRippleDrawable;

public class Ripple extends View {
    private int radius;
    private int rippleColor;
    private boolean radiusTop;
    private boolean radiusLeft;
    private boolean radiusRight;
    private boolean radiusBottom;

    public Ripple(Context context) {
        super(context);
        initialize(context, null);
    }

    public void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeView();
        initializeRipple();
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ripple);

        radius = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Ripple__radius, 0));
        rippleColor = typedArray.getColor(R.styleable.Ripple__rippleColor, Color.TRANSPARENT);
        radiusTop = typedArray.getBoolean(R.styleable.Ripple__radiusTop, true);
        radiusLeft = typedArray.getBoolean(R.styleable.Ripple__radiusLeft, true);
        radiusRight = typedArray.getBoolean(R.styleable.Ripple__radiusRight, true);
        radiusBottom = typedArray.getBoolean(R.styleable.Ripple__radiusBottom, true);

        typedArray.recycle();
    }

    private void initializeView() {
        setClickable(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initializeRipple() {
        if (rippleColor == Color.TRANSPARENT) return;

        setForeground(getRippleDrawable(rippleColor,
                getOuterRadii(
                        radiusTop && radiusLeft ? radius : 0,
                        radiusTop && radiusRight ? radius : 0,
                        radiusBottom && radiusLeft ? radius : 0,
                        radiusBottom && radiusRight ? radius : 0
                )
        ));
    }

    public Ripple(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Ripple(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }
}
