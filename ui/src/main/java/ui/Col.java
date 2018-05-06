package ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

public class Col extends LinearLayoutCompat {
    public Col(Context context) {
        super(context);
        initialize(context, null);
    }

    public Col(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Col(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs) {
        setOrientation(LinearLayoutCompat.VERTICAL);
    }
}
