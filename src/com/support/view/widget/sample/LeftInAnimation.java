package com.support.view.widget.sample;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.support.view.widget.core.AnimationImpl;

/**
 * Created by ivonhoe on 14-11-19.
 */
public class LeftInAnimation implements AnimationImpl{
    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, "translationX", 0 - parent.getWidth(), 0);
        return new ObjectAnimator[] { animator };
    }
}
