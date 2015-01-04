package com.support.widget.listview.core;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by ivonhoe on 14-11-14.
 */
public class AnimationAdapter extends BaseAdapterDecorator {

    private static final boolean DEBUG = false;
    private static final String TRANSLATION_X = "translationX";
    private static final String TRANSLATION_Y = "translationY";
    private static final String ALPHA = "alpha";
    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";

    /**
     * The fist visible position item end animation when pageViewer moving progress equals this factor.
     */
    float factor = 0.5f;

    float fistItemSlope;

    float itemsSlopeStep;

    public AnimationAdapter(BaseAdapter baseAdapter) {
        super(baseAdapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = super.getView(position, convertView, parent);

        if (convertView != null) {
            cancelExistingAnimation(convertView);
        }
        animateViewIfNecessary(position, itemView, parent);
        if (DEBUG) {
            itemView.setBackgroundColor(Color.GRAY);
        }

        //itemView.setBackgroundColor(Color.BLACK);
        return itemView;
    }

    private void animateViewIfNecessary(int position, final View view, final ViewGroup parent) {
        if (mAnimationImpl == null) {
            return;
        }
        if (position > lastAnimatedPosition) {
            if (firstAnimatedPosition == -1) {
                firstAnimatedPosition = position;
            }
        }
        if (mAnimationStartMillis == -1) {
            mAnimationStartMillis = SystemClock.uptimeMillis();
        }

        Animator[] animators = mAnimationImpl.getAnimators(parent, view);

        view.setAlpha(0);
        Animator alphaAnimator = ObjectAnimator.ofFloat(view, ALPHA, 0, 1);
        Animator[] concatAnimators = concatAnimators(animators, alphaAnimator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(concatAnimators);
        set.setStartDelay(calculateAnimationDelay(position));
        set.setDuration(300);
        set.start();

        mAnimators.put(view.hashCode(), set);
        lastAnimatedPosition = position;
    }

    /**
     * The active Animators. Keys are hashcodes of the Views that are animated.
     */
    private final SparseArray<Animator> mAnimators = new SparseArray<Animator>();
    private int firstAnimatedPosition = -1;
    private int lastAnimatedPosition = -1;
    private long mAnimationStartMillis = -1;

    private int calculateAnimationDelay(int position) {
        int delay;
        int firstVisiblePosition = mListViewWrapper.getFirstVisiblePosition();
        int lastVisiblePosition = mListViewWrapper.getLastVisiblePosition();

        int numberOfItemsOnScreen = lastVisiblePosition - firstVisiblePosition;
        int numberOfAnimatedItems = position - 1 - firstAnimatedPosition;
        if (numberOfItemsOnScreen + 1 < numberOfAnimatedItems) {
            delay = 100;
        } else {
            int delaySinceStart = (position - firstVisiblePosition) * 100;
            delay = Math.max(0, (int) (-SystemClock.uptimeMillis() + mAnimationStartMillis +
                    150 + delaySinceStart));
        }
        return delay;
    }

    private void cancelExistingAnimation(View view) {
        int hashCode = view.hashCode();
        Animator animator = mAnimators.get(hashCode);
        if (animator != null) {
            animator.end();
            mAnimators.remove(hashCode);
        }
    }

    /**
     * Merges given Animators into one array.
     */
    public static Animator[] concatAnimators(final Animator[] animators,
            final Animator alphaAnimator) {
        Animator[] allAnimators = new Animator[animators.length + 1];
        int i = 0;

        for (Animator animator : animators) {
            allAnimators[i] = animator;
            ++i;
        }

        allAnimators[allAnimators.length - 1] = alphaAnimator;
        return allAnimators;
    }
}
