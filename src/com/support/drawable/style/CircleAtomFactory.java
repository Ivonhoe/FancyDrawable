package com.support.drawable.style;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;

/**
 * Created by Fan.Yang on 2015/1/7.
 */
public class CircleAtomFactory extends AtomFactory {

    private static double PI = Math.PI / 180;
    private static float startDegree = 0f;
    private static float endDegree = 360f;

    @Override
    public ObjectAnimator getAtomAnimator(final Atom atom, final Rect bound) {
        if (atom == null || bound == null)
            return null;
        ObjectAnimator animator = ObjectAnimator.ofFloat(atom, "delta", startDegree, endDegree);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) (bound.centerX() +
                        (bound.height() / 2) * Math.sin(PI * atom.getDelta()));
                float y = (float) (bound.centerY() -
                        (bound.height() / 2) * Math.cos(PI * atom.getDelta()));
                atom.setLocationX(x);
                atom.setLocationY(y);
            }
        });
        return animator;
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return atom.getDelta() > startDegree && atom.getDelta() < endDegree;
    }
}
