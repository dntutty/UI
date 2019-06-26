package com.dntutty.ui.canvas;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PathMeasureWithTouchView extends View {
    private Path mPath;
    public PathMeasureWithTouchView(Context context) {
        this(context,null);
    }

    public PathMeasureWithTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMeasureWithTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
