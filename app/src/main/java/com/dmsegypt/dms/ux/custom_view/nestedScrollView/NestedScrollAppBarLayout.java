package com.dmsegypt.dms.ux.custom_view.nestedScrollView;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Nested scroll app bar layout.
 * */

@CoordinatorLayout.DefaultBehavior(NestedScrollAppBarLayout.Behavior.class)
public class NestedScrollAppBarLayout extends AppBarLayout
        implements NestedScrollingChild {
    // widget
    private NestedScrollingChildHelper nestedScrollingChildHelper;

    /** <br> life cycle. */

    public NestedScrollAppBarLayout(Context context) {
        super(context);
        this.initialize();
    }

    public NestedScrollAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    private void initialize() {
        this.nestedScrollingChildHelper = new NestedScrollingChildHelper(this);

        setNestedScrollingEnabled(true);
    }

    /** <br> interface. */

    // nested scrolling child.

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return nestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedScroll(
                dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    /** <br> inner class. */

    public static class Behavior extends AppBarLayout.Behavior {
        // data
        private float oldY;

        // life cycle.

        public Behavior() {
            super();
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        // touch.

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
            return super.onInterceptTouchEvent(parent, child, ev);
        }

        @Override
        public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    child.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                    oldY = ev.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if ((child.getY() >= 0 && ev.getY() > oldY)
                            || (parent.getTranslationY() > 0 && ev.getY() < oldY)) {
                        int[] total = new int[] {0, (int) (oldY - ev.getY())};
                        int[] consumed = new int[] {0, 0};
                        child.dispatchNestedPreScroll(total[0], total[1], consumed, null);
                        child.dispatchNestedScroll(0, 0, total[0] - consumed[0], total[1] - consumed[1], null);
                    }
                    oldY = ev.getY();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    child.stopNestedScroll();
                    break;
            }

            return parent.getTranslationY() > 0 || super.onTouchEvent(parent, child, ev);
        }
    }
}
