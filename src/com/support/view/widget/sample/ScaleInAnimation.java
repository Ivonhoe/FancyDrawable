package com.support.view.widget.sample;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.support.view.widget.core.AnimationImpl;

/**
 * Created by ivonhoe on 14-11-19.
 */
public class ScaleInAnimation implements AnimationImpl {
    private static final float DEFAULT_SCALE_FROM = 0.8f;

    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";

    private final float mScaleFrom;

    public ScaleInAnimation() {
        mScaleFrom = DEFAULT_SCALE_FROM;
    }

    public ScaleInAnimation(float scaleFrom) {
        mScaleFrom = scaleFrom;
    }

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, SCALE_X, mScaleFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, SCALE_Y, mScaleFrom, 1f);
        return new ObjectAnimator[] { scaleX, scaleY };
    }
}
