package ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

public class Row extends LinearLayoutCompat {
    public Row(Context context) {
        super(context);
        initialize(context, null);
    }

    public Row(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Row(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs) {
        setOrientation(LinearLayoutCompat.HORIZONTAL);
    }
}
