package com.dntutty.ui.materialdesign.customrecyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * 仿照HideBottomViewOnScrollBehavior所写
 * 动画也可通过ViewCompat.animate()实现
 *
 * @param <V>
 */
public class ScaleBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    protected static final int ENTER_ANIMATION_DURATION = 225;
    protected static final int EXIT_ANIMATION_DURATION = 175;
    private boolean isRunning;
    private ViewPropertyAnimator currentAnimator;
    private static final int STATE_SCROLLED_HIDE = 1;
    private static final int STATE_SCROLLED_SHOW = 2;
    private int currentState = STATE_SCROLLED_SHOW;
    private int height;

    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        this.height = child.getMeasuredHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;//垂直滚动
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (currentState != STATE_SCROLLED_HIDE && dyConsumed > 0) {
            scaleHide(child);
        } else if (currentState != STATE_SCROLLED_SHOW && dyConsumed < 0) {
            scaleShow(child);
        }
    }

    private void scaleShow(final V child) {
        if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
            child.clearAnimation();
        }

        this.currentState = STATE_SCROLLED_SHOW;
        this.animateChildTo(child, 1, 0, ENTER_ANIMATION_DURATION, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
    }

    private void scaleHide(final V child) {
        if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
            child.clearAnimation();
        }

        this.currentState = STATE_SCROLLED_HIDE;
        this.animateChildTo(child, 0, height, EXIT_ANIMATION_DURATION, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);

    }

    private void animateChildTo(V child, float scale, int targetY, long duration, TimeInterpolator interpolator) {
        this.currentAnimator = child.animate().scaleX(scale).scaleY(scale).translationY((float) targetY).setInterpolator(interpolator).setDuration(duration).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ScaleBehavior.this.currentAnimator = null;
            }
        });
    }
}
