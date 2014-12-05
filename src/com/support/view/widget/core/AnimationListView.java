package com.support.view.widget.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.support.view.widget.control.AnimationController;
import com.support.view.widget.control.Location;
import com.support.view.widget.control.PageScrollingListener;
import com.support.view.widget.utils.L;

/**
 * Created by ivonhoe on 14-11-14.
 */
public class AnimationListView extends ListView implements PageScrollingListener {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;
    private static final int ANIMATION_DURATION = 400;

    private Context mContext;

    private AnimationImpl animationImpl;

    private AnimationAdapter animationAdapter;

    /**
     * The fist visible position or (指定的)item end animation when pageViewer moving progress equals this factor.
     */
    private static final float factor = 0.3f;

    private float itemsSlopeStep;
    private int arrow = -1;
    private int visibleItemsCount;
    private int maxYOverScrollDistance;
    private int reboundTime;
    private int[] temp = new int[2];

    // location of view center
    private Location[] locations;

    private boolean isActionFinish;

    private boolean isOverScrolling;

    private boolean enableOverScroll;

    private boolean isPageMoving;

    private Interpolator slopeInterpolator;

    private Rect mTempRect;

    private Drawable mDivider;

    private Runnable resetOverScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY = getScrollY();
            if (scrollY != 0 && isActionFinish) {
                ReboundAnimation reboundAnimation = new ReboundAnimation(scrollY, 0);
                reboundAnimation.setDuration(reboundTime);
                startAnimation(reboundAnimation);
                reboundAnimation.setAnimationListener(new ReboundListener());
                //L.d("********resetOverScrollRunnable,scrollY=" + scrollY);
            }
        }
    };

    public AnimationListView(Context context) {
        super(context);

        mContext = context;
        initAnimationListView();
    }

    public AnimationListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        initAnimationListView();
    }

    public AnimationListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        initAnimationListView();
    }

    private void initAnimationListView() {
        //get the density of the screen and do some maths with it on the max overScroll distance
        //variable so that you get similar behaviors no matter what the screen size

        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        maxYOverScrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
        reboundTime = ANIMATION_DURATION;
        slopeInterpolator = new LinearInterpolator();
        mTempRect = new Rect();
		isOverScrolling = false;
        enableOverScroll = true;

        locations = new Location[AnimationController.SAMPLE_SIZE];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new Location();
            locations[i].clear();
        }

        Drawable divider = getDivider();
        if (divider != null) {
            mDivider = divider.getConstantState().newDrawable();
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // create adapter decorator
        animationAdapter = new AnimationAdapter((BaseAdapter) adapter);
        // set listView wrapper
        animationAdapter.setAbsListView(this);
        animationAdapter.setAnimatorWrapper(animationImpl);

        super.setAdapter(animationAdapter);
    }

    public void attachAnimators(AnimationImpl animationImpl) {
        this.animationImpl = animationImpl;
        if (animationAdapter != null) {
            animationAdapter.setAnimatorWrapper(animationImpl);
        }
    }

    public void setAnimationEnable(boolean enable) {

    }

    public void setOverScrollAnimationEnable(boolean enable) {
        enableOverScroll = enable;
    }

    @Override
    public Location[] onFindPageScroll(float progress) {
        if (getVisibility() != VISIBLE) {
            return null;
        }

        if (progress == 0) {
            clearLocationCache();
        } else {
            isPageMoving = true;
        }

        getLocationOnScreen(temp);
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].isClear()) {
                locations[i].x = (temp[0] + getWidth() / 2f);
                locations[i].y = temp[1];
                if (i == locations.length - 1) {
                    return locations;
                }
                return null;
            }

        }

        return locations;
    }

    /*
    * Page scroll from left to right,pageScrolling in [0,1]
    * Page scroll from right to left, pageScrolling in [-1,0]
    * */
    @Override
    public void onPageScrolling(float pageScrolling, float y) {
        float progress = pageScrolling > 0 ? pageScrolling : 1f + pageScrolling;

        computeTouchedItemIndex(y - locations[0].y);
        int number = Math.max(arrow, visibleItemsCount - arrow);
        itemsSlopeStep = (1f - factor) / (number * factor);

        float y1;
        for (int i = 0; i < visibleItemsCount; i++) {
            View itemView = getChildAt(i);

            int delta = Math.abs(arrow - i);
            y1 = Math.min(1f, (1f / factor - itemsSlopeStep * delta) * progress);
            //y1 = slopeInterpolator.getInterpolation(pageScrolling);
            float transX = getWidth() - getWidth() * y1;
            if (pageScrolling > 0) {
                itemView.setTranslationX(transX);
            } else {
                itemView.setTranslationX(-transX);
            }
        }
        arrow = progress == 0 || progress == 1 ? -1 : arrow;
        invalidate();
    }

    @Override
    public void onEndPageScroll() {
        clearLocationCache();
        isPageMoving = false;
    }

    private void clearLocationCache() {
        for (int i = 0; i < locations.length; i++) {
            locations[i].clear();
        }
    }

    private int computeTouchedItemIndex(float y) {
        int firstVisiblePosition = getFirstVisiblePosition();
        int lastVisiblePosition = getLastVisiblePosition();
        visibleItemsCount = lastVisiblePosition - firstVisiblePosition + 1;
        if (arrow != -1) {
            return arrow;
        }

        if (y < 0 || y > getHeight()) {
            arrow = 0;
            return arrow;
        }

        arrow = Math.max((int) y * visibleItemsCount / getHeight(), 0);
        return arrow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            computeTouchedItemIndex(ev.getY());
            isActionFinish = false;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            isActionFinish = true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        if (!enableOverScroll) {
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                    maxOverScrollX, maxOverScrollY, isTouchEvent);
        }

        getHandler().removeCallbacks(resetOverScrollRunnable);
        isOverScrolling = true;
        postDelayed(resetOverScrollRunnable, 80);
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own
        //custom variable mMaxYOverScrollDistance;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxYOverScrollDistance, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (isOverScrolling) {
            float space = Math.abs(t);
            float delta = 0;
            //scrollY < 0, overScroll on head
            for (int i = arrow; i >= 1 && t <= 0; i--) {
                View viewItem = getChildAt(i - 1);
                delta = delta + 2 * space * (arrow + 1 - i) / (arrow * (arrow + 1));
                viewItem.setTranslationY(-delta);
            }

            //scrollY >0 ,overScroll on foot
            for (int i = arrow; i < getChildCount() && t >= 0; i++) {
                // View viewItem = getChildAt(i - 1);
                // delta = delta + 2 * space * (arrow + 1 - i) / (arrow * (arrow + 1));
                // viewItem.setTranslationY(-delta);
            }
        }

        if (t == 0 && isActionFinish) {
            arrow = -1;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        enableClonedDivider(isOverScrolling || isPageMoving);
        super.dispatchDraw(canvas);

        if (isOverScrolling || isPageMoving) {
            final Rect bounds = mTempRect;
            int bottom;
            float translationX;
            float translationY;
            int firstVisiblePosition = getFirstVisiblePosition();
            int lastVisiblePosition = getLastVisiblePosition();
            visibleItemsCount = lastVisiblePosition - firstVisiblePosition + 1;

            // 简单版本
            for (int i = 0; i < visibleItemsCount; i++) {
                View child = getChildAt(i);
                translationX = child.getTranslationX();
                translationY = child.getTranslationY();
                bottom = child.getBottom() + (int) translationY;
                bounds.left = (int) (getPaddingLeft() + translationX);
                bounds.top = bottom;
                bounds.right = (int) (getRight() - getLeft() - getPaddingRight() + translationX);
                bounds.bottom = bottom + getDividerHeight();
                mDivider.setBounds(bounds);
                mDivider.draw(canvas);
            }
            // 重写版本...
        }
    }

    @Override
    public void setDivider(Drawable divider) {
        super.setDivider(divider);
        if (divider != null) {
            mDivider = divider.getConstantState().newDrawable();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimationController.getInstance(getContext()).register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnimationController.getInstance(getContext()).unregister(this);
    }

    private void enableClonedDivider(boolean enable) {
        Drawable divider = getDivider();
        if (divider != null) {
            divider.setAlpha(enable ? 0 : 255);
            mDivider.setAlpha(enable ? 255 : 0);
        }
    }

    private class ReboundAnimation extends Animation {
        private final int mStartY;
        private final int mDeltaY;

        public ReboundAnimation(int startY, int endY) {
            mStartY = startY;
            mDeltaY = endY - startY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            int newDelta = (int) (mDeltaY * interpolatedTime);
            overScrollBy(0, newDelta, 0, mStartY, 5, 5, 0, maxYOverScrollDistance, true);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private class ReboundListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isOverScrolling = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
