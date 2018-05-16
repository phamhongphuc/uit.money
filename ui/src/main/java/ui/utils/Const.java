package ui.utils;

import android.view.Gravity;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Const {
    public static final String NONE = "";
    public static final String ICON = "fonts/aicon.ttf";
    public static final String REGULAR = "fonts/segoe regular.ttf";
    public static final String LIGHT = "fonts/segoe light.ttf";
    public static final String BOLD = "fonts/segoe bold.ttf";
    public static final Map<Integer, String> FONTs = ImmutableMap.<Integer, String>builder()
            .put(0, NONE)
            .put(1, LIGHT)
            .put(2, REGULAR)
            .put(3, BOLD)
            .build();

    private static final int LEFT = 0;
    public static final int CENTER = 1;
    private static final int RIGHT = 2;
    public static final Map<Integer, Integer> ALIGN = ImmutableMap.<Integer, Integer>builder()
            .put(LEFT, Gravity.START)
            .put(CENTER, Gravity.CENTER_HORIZONTAL)
            .put(RIGHT, Gravity.END)
            .build();

    private static final float SMALL = 0.35f;
    private static final float LARGE = 0.6f;
    private static final float MEDIUM = 0.45f;
    public static final Map<Integer, Float> FONT_SIZEs = ImmutableMap.<Integer, Float>builder()
            .put(0, SMALL)
            .put(1, LARGE)
            .put(2, MEDIUM)
            .build();

    public static final int RADIUS_NONE = 0;
    public static final int RADIUS_ICON = 1;
    public static final int RADIUS_TEXT = 2;
}
