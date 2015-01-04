package com.support.widget.listview.core;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.*;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.support.animation.FreePathInterpolator;
import com.support.widget.listview.control.AnimationController;
import com.support.widget.listview.control.Location;
import com.support.widget.listview.control.PageScrollingListener;
import com.support.widget.listview.utils.L;

import java.lang.reflect.Field;

/**
 * Created by ivonhoe on 14-11-14.
 */
public class AnimationListView extends ListView implements PageScrollingListener {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 13;
    private static final int ANIMATION_DURATION = 400;
    private static final boolean DEFAULT_OVERSCROLL_ENABLE = true;
    private static final boolean DEFAULT_ITEM_ANIMATION_ENABLE = false;

    private Context mContext;

    private DisplayMetrics metrics;

    private AnimationImpl animationImpl;

    private AnimationAdapter animationAdapter;

    /**
     * The fist visible position or (指定的)item end animation when pageViewer moving progress equals this factor.
     */
    private static final float factor = 0.3f;

    private int arrow = -1;
    private int visibleItemsCount;
    private int maxYOverScrollDistance;
    private int reboundTime;
    private int[] temp = new int[2];

    // location of view center
    private Location[] locations;

    private boolean isActionFinish;

    private boolean isOverScrolling;

    private boolean isPageMoving;

    private boolean enableClonedDivider = false;

    private boolean enableOverScroll;

    private boolean allowOverScroll;

    private boolean enableItemAnimation;

    private Rect mTempRect;

    private Drawable mDivider;

    private Runnable resetOverScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY = getScrollY();
            if (scrollY != 0 && isActionFinish) {
                ReboundAnimation reboundAnimation = new ReboundAnimation(scrollY, 0);
                reboundAnimation.setDuration(reboundTime);
                reboundAnimation.setInterpolator(new AccelerateInterpolator());
                startAnimation(reboundAnimation);
                reboundAnimation.setAnimationListener(new ReboundListener());
            }
            if (scrollY == 0)
                isOverScrolling = false;
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
        metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        maxYOverScrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
        reboundTime = ANIMATION_DURATION;
        mTempRect = new Rect();
        isOverScrolling = false;
        enableOverScroll = DEFAULT_OVERSCROLL_ENABLE;
        enableItemAnimation = DEFAULT_ITEM_ANIMATION_ENABLE;

        locations = new Location[AnimationController.SAMPLE_SIZE];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new Location();
            locations[i].clear();
        }

        Drawable divider = getDivider();
        if (divider != null) {
            mDivider = divider.getConstantState().newDrawable();
        }

        //setBackground(new ColorDrawable(Color.WHITE));

        removeOverscrollEffect();

        setupScrollInterpolator();
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
        enableItemAnimation = enable;
    }

    public void setOverScrollAnimationEnable(boolean enable) {
        enableOverScroll = enable;
    }

    @Override
    public Location[] onFindPageScroll(float progress, float rawY) {
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
                if (i != locations.length - 1) {
                    return null;
                }
            }
        }

        computeTouchedItemIndex(rawY);
        return locations;
    }

    /*
    * Page scroll from left to right,pageScrolling in [0,1]
    * Page scroll from right to left, pageScrolling in [-1,0]
    * */
    @Override
    public void onPageScrolling(float pageScrolling) {
        /*float progress = pageScrolling > 0 ? pageScrolling : 1f + pageScrolling;

        int number = Math.max(arrow, visibleItemsCount - arrow);
        itemsSlopeStep = (1f - factor) / (number * factor);

        float y1;
        for (int i = 0; i < visibleItemsCount; i++) {
            View itemView = getChildAt(i);

            int delta = Math.abs(arrow - i);
            y1 = Math.min(1f, (1f / factor - itemsSlopeStep * delta) * progress);
            float transX = getWidth() - getWidth() * y1;
            if (pageScrolling > 0) {
                itemView.setTranslationX(transX);
            } else {
                itemView.setTranslationX(-transX);
            }
        }
        arrow = progress == 0 || progress == 1 ? -1 : arrow;
        invalidate();*/
        onPageScrolling2(pageScrolling);
    }

    private void onPageScrolling2(float pageScrolling) {
        float progress = pageScrolling > 0 ? pageScrolling : 1f + pageScrolling;

        int number = Math.max(arrow, visibleItemsCount - arrow);
        float step = mFreePathInterpolator.length / number;

        float y1;
        for (int i = 0; i < visibleItemsCount; i++) {
            View itemView = getChildAt(i);

            int delta = Math.min(mFreePathInterpolator.length - 1, (int) (Math.abs(arrow - i) * step));
            y1 = mFreePathInterpolator[delta].getInterpolation(progress);
            float transX = getWidth() - getWidth() * Math.min(1f, y1);
            if (pageScrolling > 0) {
                itemView.setTranslationX(transX);
            } else {
                itemView.setTranslationX(-transX);
            }
        }
        arrow = progress == 0 || progress == 1 ? -1 : arrow;
        invalidate();
    }

    Interpolator[] mFreePathInterpolator = new Interpolator[12];
    float[][] controls = {
            {0.05f, 0.7f, 0.28f, 0.93f},
            {0.13f, 0.66f, 0.31f, 0.87f},
            {0.19f, 0.61f, 0.36f, 0.81f},
            {0.24f, 0.35f, 0.39f, 0.77f},
            {0.28f, 0.39f, 0.42f, 0.72f},
            {0.32f, 0.49f, 0.45f, 0.69f},
            {0.35f, 0.46f, 0.47f, 0.65f},
            {0.37f, 0.44f, 0.49f, 0.61f},
    };

    static double PI = Math.PI / 180;
    static float start = 87;
    static float end = 50;
    static float R1 = 0.7f;
    static float R2 = 0.7f;
    private void setupScrollInterpolator() {
        float x1,y1,x2,y2;
        float step = (start - end) / mFreePathInterpolator.length;
        for (int i = 0; i < mFreePathInterpolator.length; i++) {
            x1 = (float) Math.cos(PI * (start - step * i)) * R1;
            y1 = (float) Math.sin(PI * (start - step * i)) * R1;
            x2 = 1f - ((float) Math.sin(PI * (start - step * i))) * R2;
            y2 = 1f - ((float) Math.cos(PI * (start - step * i))) * R2;
            mFreePathInterpolator[i] = new FreePathInterpolator(x1, y1, x2, y2,
                    FreePathInterpolator.BEZIER, FreePathInterpolator.DISPLACEMENT);
        }
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

    private int computeTouchedItemIndex(float rawY) {
        int firstVisiblePosition = getFirstVisiblePosition();
        int lastVisiblePosition = getLastVisiblePosition();
        visibleItemsCount = lastVisiblePosition - firstVisiblePosition + 1;
        int itemsCount = animationAdapter.getCount();
        allowOverScroll = (firstVisiblePosition == 0 || lastVisiblePosition == itemsCount - 1) ? true : false;
        arrow = 0;
        for (int i = 0; i < visibleItemsCount; i++) {
            getChildAt(i).getGlobalVisibleRect(mTempRect);
            if (mTempRect.bottom >= rawY) {
                arrow = i;
                break;
            }
        }

        maxYOverScrollDistance = (int) (metrics.density * MAX_Y_OVERSCROLL_DISTANCE *
                (firstVisiblePosition == 0 ? Math.min(arrow, 2) : Math.min(visibleItemsCount - arrow - 1, 2)));
        return arrow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            computeTouchedItemIndex(ev.getRawY());
            isActionFinish = false;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            isActionFinish = true;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
                                   boolean isTouchEvent) {
        if (!enableOverScroll || !allowOverScroll) {
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
            int count = getChildCount() - arrow;
            float space = Math.abs(t);
            float delta = 0;
            //scrollY < 0, overScroll on head
            for (int i = arrow; i >= 0 && t <= 0; i--) {
                View viewItem = getChildAt(i - 1);
                if (viewItem != null) {
                    delta = delta + 2 * space * (arrow + 1 - i) / ((arrow + 1) * (arrow + 2));
                    viewItem.setTranslationY(-delta);
                }
            }

            //scrollY >0 ,overScroll on foot
            delta = 0;
            for (int i = 1; i < count && t >= 0; i++) {
                View viewItem = getChildAt(i + arrow);
                if (viewItem != null) {
                    // delta = delta + 2 * space * (count - i) / (count * (count - 1));
                    delta = delta + 2 * space * i / ((count ) * (count - 1));
                    viewItem.setTranslationY(delta);
                }
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
            int bottomTop;
            float translationX;
            float translationY;
            visibleItemsCount = getLastVisiblePosition() - getFirstVisiblePosition() + 1;

            // 简单版本
            for (int i = 0; i < visibleItemsCount; i++) {
                View child = getChildAt(i);
                translationX = child.getTranslationX();
                translationY = child.getTranslationY();
                bottomTop = child.getBottom() + (int) translationY;
                bounds.left = (int) (getPaddingLeft() + translationX);
                bounds.top = bottomTop;
                bounds.right = (int) (getRight() - getLeft() - getPaddingRight() + translationX);
                bounds.bottom = bottomTop + getDividerHeight();
                mDivider.setBounds(bounds);
                mDivider.draw(canvas);
            }
        }
    }

    Camera mCamera = new Camera();
    Matrix mMatrix = new Matrix();

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
        if (divider != null && enableClonedDivider != enable) {
            divider.setAlpha(enable ? 0 : 255);
            mDivider.setAlpha(enable ? 255 : 0);
            enableClonedDivider = enable;
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

    /*
    * Based on http://stackoverflow.com/questions/7777803/listview-top-highlight-on-scrolling
    * */
    private void removeOverscrollEffect() {
        try {
            Class<?> superClass = getClass().getSuperclass().getSuperclass();

            Field field = superClass.getDeclaredField("mEdgeGlowTop");
            field.setAccessible(true);
            Object edgeGlowTop = field.get(this);
            if (edgeGlowTop != null) {
                Class<? extends Object> edgeClass = edgeGlowTop.getClass();
                Field edgeDrawable = edgeClass.getDeclaredField("mEdge");
                edgeDrawable.setAccessible(true);
                edgeDrawable.set(edgeGlowTop, new ColorDrawable(Color.TRANSPARENT));
                Field glowDrawable = edgeClass.getDeclaredField("mGlow");
                glowDrawable.setAccessible(true);
                glowDrawable.set(edgeGlowTop, new ColorDrawable(Color.TRANSPARENT));
                field.set(this, edgeGlowTop);
            }

            Field fieldBottom = superClass.getDeclaredField("mEdgeGlowBottom");
            fieldBottom.setAccessible(true);
            Object edgeGlowBottom = fieldBottom.get(this);
            if (edgeGlowBottom != null) {
                Class<? extends Object> edgeClassBottom = edgeGlowBottom.getClass();
                Field edgeDrawableBottom = edgeClassBottom.getDeclaredField("mEdge");
                edgeDrawableBottom.setAccessible(true);
                edgeDrawableBottom.set(edgeGlowBottom, new ColorDrawable(Color.TRANSPARENT));
                Field glowDrawableBottom = edgeClassBottom.getDeclaredField("mGlow");
                glowDrawableBottom.setAccessible(true);
                glowDrawableBottom.set(edgeGlowBottom, new ColorDrawable(Color.TRANSPARENT));
                fieldBottom.set(this, edgeGlowBottom);
            }
        } catch (Exception e) {
            L.e(e.getMessage());
        }
    }

    // 1.listview 第一个item也需要位移 [OK]
    // 2.只有当listiew 滑动起始位置在最上面的时候时才允许 overscroll [OK]
    // 3.当overscroll结束时继续滑动，需要增加百叶窗3D效果 [NOK]
}
