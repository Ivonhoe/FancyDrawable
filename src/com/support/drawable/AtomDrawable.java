package com.support.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.animation.Interpolator;
import com.support.animation.FreePathInterpolator;
import com.support.drawable.style.Atom;
import com.support.drawable.style.AtomFactory;
import com.support.drawable.style.AtomStyle;
import com.support.drawable.style.HorizontalAtomFactory;
import com.support.widget.listview.utils.L;

/**
 * Created by ivonhoe on 2014/12/24.
 */
public class AtomDrawable extends AnimationDrawable {

    private int reachEnd = 0;

    private Atom[] mAtoms;
    private AtomStyle mAtomStyle;
    private AtomFactory mAtomFactory;
    private AnimatorSet mAnimatorSet;

    public AtomDrawable() {
        Interpolator bezierInterpolator = new FreePathInterpolator(0.03f, 0.615f, 0.995f, 0.415f,
                FreePathInterpolator.BEZIER, FreePathInterpolator.DISPLACEMENT);

        AtomStyle style = new AtomStyle();
        style.setInterpolator(bezierInterpolator);
        initialize(style, style.getAtomFactory());
    }

    public AtomDrawable(AtomStyle style) {
        initialize(style, style.getAtomFactory());
    }

    private void initialize(AtomStyle style, AtomFactory factory) {
        mAtomStyle = style;
        mAtomFactory = factory;
        int atomCount = mAtomStyle.getSectionCount();
        mAtoms = new Atom[atomCount];
        for (int i = 0; i < atomCount; i++) {
            mAtoms[i] = new Atom();
        }
        mAnimatorSet = null;
    }

    @Override
    public void onStart() {
        if (mBounds != null) {
            setupAnimators(mBounds);
        }
    }

    private void setupAnimators(final Rect bound) {
        if (mAnimatorSet == null && mAtomStyle != null && mAtomFactory != null) {
            mAtomStyle.setParentBound(bound);
            mAnimatorSet = mAtomFactory.getAtomsAnimation(mAtomStyle, mAtoms);
            mAnimatorSet.start();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        //setupAnimators(mBounds);

        for (int i = 0; i < mAtomStyle.getSectionCount(); i++) {
            if (mAtoms[i].getLocationX() > 0 && (int) mAtoms[i].getLocationX() < mBounds.width()) {
                mAtomFactory.drawAtom(canvas, mAtomStyle, mAtoms[i]);
            } else if ((int) mAtoms[i].getLocationX() == mBounds.width()) {
                mAtoms[i].setLocationX(-1f);
                reachEnd++;
            }
        }
    }

    @Override
    public void onUpdate() {
        if (mAnimatorSet != null && !mAnimatorSet.isRunning() &&
                reachEnd > mAtomStyle.getSectionCount() - 1) {
            reachEnd = 0;
            mAnimatorSet.start();
        }
    }
}
