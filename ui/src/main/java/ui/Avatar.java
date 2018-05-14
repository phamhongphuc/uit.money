package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.facebook.internal.ImageRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import ui.ui.R;

import static android.graphics.PorterDuff.Mode.SRC_IN;

public class Avatar extends android.support.v7.widget.AppCompatImageView {
    private final Picasso picasso = Picasso.with(getContext());
    private int outerSize = 0;
    private int innerSize = 0;
    private String fbid;

    public Avatar(Context context) {
        super(context);
        initialize(context, null);
    }


    public Avatar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Avatar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs) {
        initializeAttr(context, attrs);
        initializeImage();
    }

    private void initializeAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Avatar);
        fbid = typedArray.getString(R.styleable.Avatar__fbid);
        innerSize = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Avatar__innerSize, 0));
        outerSize = MeasureSpec.getSize((int) typedArray.getDimension(R.styleable.Avatar__outerSize, 0));
        typedArray.recycle();
    }

    private void initializeImage() {
        if (fbid == null) return;
        if (outerSize == 0 || innerSize == 0) return;

        Uri src = ImageRequest.getProfilePictureUri(fbid, innerSize, innerSize);
        picasso
                .load(src)
                .resize(innerSize, innerSize)
                .transform(new CircleTransform())
                .into(this);
        picasso.setIndicatorsEnabled(true);
    }

    /**
     * Data binding: set_userId
     *
     * @param fbid String
     */
    public void set_fbid(String fbid) {
        this.fbid = fbid;
        initializeImage();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null || outerSize == 0) return;

        Bitmap source = ((BitmapDrawable) drawable).getBitmap();
        canvas.drawBitmap(source, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(outerSize, outerSize);
        setMeasuredDimension(outerSize, outerSize);
    }

    private class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap input) {
            Bitmap source = Bitmap.createBitmap(input, 0, 0, innerSize, innerSize);
            if (source != input) input.recycle();
            Bitmap output = Bitmap.createBitmap(outerSize, outerSize, input.getConfig());

            int center = outerSize / 2;
            int radius = innerSize / 2;
            int start = center - radius;
            int end = center + radius;
            final Rect rect = new Rect(start, start, end, end);
            final Paint paint = new Paint();
            final Canvas canvas = new Canvas(output);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            paint.setColor(Color.WHITE);
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawCircle(center, center, radius, paint);

            paint.setXfermode(new PorterDuffXfermode(SRC_IN));
            canvas.drawBitmap(source, null, rect, paint);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}