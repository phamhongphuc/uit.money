package ui.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class Calc {
    public static void setShadow(@NonNull ShadowView view, Paint paint) {
        final Drawable background = view.getView().getBackground();
        final int color = background instanceof ColorDrawable
                ? ((ColorDrawable) background).getColor()
                : Color.TRANSPARENT;

        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(
                view.getShadowSize(),
                view.getDx(),
                view.getDy(),
                view.getShadowColor()
        );
    }

    public static RippleDrawable getRippleDrawable(int rippleColor, float left, float right) {
        return getRippleDrawable(rippleColor, getOuterRadii(left, right));
    }

    public static RippleDrawable getRippleDrawable(int rippleColor, float[] outerRadii) {
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        return new RippleDrawable(
                ColorStateList.valueOf(rippleColor),
                null,
                new ShapeDrawable(roundRectShape)
        );
    }

    private static float[] getOuterRadii(float left, float right) {
        return new float[]{
                left, left, right, right, right, right, left, left
        };
    }

    public static float[] getOuterRadii(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        return new float[]{
                topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft
        };
    }

    @NonNull
    public static PaintDrawable getPaintDrawable(@ColorInt int color, float left, float right) {
        final PaintDrawable paintDrawable = new PaintDrawable(color);
        paintDrawable.setCornerRadii(getOuterRadii(left, right));

        return paintDrawable;
    }
}
