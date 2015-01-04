package com.support.widget.listview.sample;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.support.widget.listview.core.AnimationImpl;

/**
 * Created by ivonhoe on 14-11-19.
 */
public class BottomInAnimation implements AnimationImpl {

    private static final String TRANSLATION_Y = "translationY";

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, TRANSLATION_Y, parent.getMeasuredHeight() >> 1, 0);
        return new Animator[]{animator};
    }
}
