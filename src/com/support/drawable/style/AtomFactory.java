package com.support.drawable.style;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import static com.support.drawable.style.AtomStyle.Shape.CHAR;

/**
 * Created by Fan.Yang on 2015/1/7.
 */
public abstract class AtomFactory {

    public AnimatorSet getAtomsAnimation(AtomStyle atomStyle, Atom[] atoms) {
        AnimatorSet animatorSet = new AnimatorSet();
        int count = atomStyle.getSectionCount();
        Animator[] animators = new Animator[count];
        String text = atomStyle.getText();
        final Rect bound = atomStyle.getParentBound();
        if (bound == null) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            final Atom atom = atoms[i];
            if (!TextUtils.isEmpty(text)) {
                atom.setText(text.substring(text.length() - i - 1, text.length() - i));
            }
            ObjectAnimator animator = getAtomAnimator(atoms[i], bound);
            if (animator != null) {
                animator.setStartDelay(atomStyle.getDelay() * (i + 1));
                animators[i] = animator;
            }
        }
        animatorSet.playTogether(animators);
        animatorSet.setDuration(atomStyle.getDuration());
        animatorSet.setInterpolator(atomStyle.getInterpolator());
        return animatorSet;
    }

    public abstract ObjectAnimator getAtomAnimator(final Atom atom, final Rect bound);

    public void drawAtom(Canvas canvas, AtomStyle style, Atom atom) {
        Paint paint = style.getPaint();
        AtomStyle.Shape shape = style.getShape();

        switch (shape) {
            case CHAR:
                canvas.drawText(atom.getText(), atom.getLocationX(), atom.getLocationY(), paint);
                break;

            case POINT:
                canvas.drawPoint(atom.getLocationX(), atom.getLocationY(), paint);
                break;

        }
    }
}
