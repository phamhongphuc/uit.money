package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import ui.ui.R;

public class List extends RecyclerView {
    private boolean stackFromEnd;

    public List(Context context) {
        super(context);
        initialize(context, null);
    }

    public List(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public List(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);

    }

    private void initializeAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.List);
        stackFromEnd = typedArray.getBoolean(R.styleable.List__stackFromEnd, false);

        typedArray.recycle();
    }

    private void initialize(Context context, AttributeSet attrs) {
        initializeAttrs(context, attrs);
        initializeSelf(context);
    }

    private void initializeSelf(Context context) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(stackFromEnd);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
        setClipToPadding(false);
        setClipToOutline(false);
    }
}
