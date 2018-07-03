package ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

public class ListWrap extends RecyclerView {
    public ListWrap(Context context) {
        super(context);
        initialize(context);
    }

    public ListWrap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ListWrap(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);

    }

    private void initialize(Context context) {
        initializeSelf(context);
    }

    private void initializeSelf(Context context) {
        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        layoutManager.setFlexWrap(FlexWrap.WRAP);

        setLayoutManager(layoutManager);
        setHasFixedSize(true);
        setClipToPadding(false);
        setClipToOutline(false);
    }
}
