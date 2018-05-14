package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import ui.ui.R;

public class Drawer extends DrawerLayout {
    private ObservableBoolean isOpen = new ObservableBoolean(false);

    public Drawer(@NonNull Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        initializeAttrs(context, attrs);
        initializeListener();
    }

    private void initializeAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Drawer);
        isOpen.set(typedArray.getBoolean(R.styleable.Drawer__isOpen, false));

        typedArray.recycle();
    }

    private void initializeListener() {
        addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isOpen.set(true);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                isOpen.set(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public Drawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Drawer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    @BindingAdapter(value = "_isOpenAttrChanged")
    public static void setListener(Drawer drawer, final InverseBindingListener listener) {
        if (drawer != null) {
            drawer.isOpen.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("_isOpen")
    public static void set_isOpen(Drawer drawer, boolean isOpen) {
        drawer.isOpen.set(isOpen);
        if (drawer.isOpen.get()) {
            drawer.openDrawer(Gravity.START);
        } else {
            drawer.closeDrawer(Gravity.START);
        }
    }

    @InverseBindingAdapter(attribute = "_isOpen")
    public static Boolean get_isOpen(Drawer drawer) {
        return drawer.isOpen.get();
    }
}