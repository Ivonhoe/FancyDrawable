package com.support.drawable.style;

import android.animation.ObjectAnimator;
import android.graphics.Rect;

/**
 * Created by Fan.Yang on 2015/1/7.
 */
public class HorizontalAtomFactory extends AtomFactory {

    int width;

    @Override
    public ObjectAnimator getAtomAnimator(Atom atom, Rect parentBound) {
        if (atom == null || parentBound == null)
            return null;

        width = parentBound.width();
        atom.setLocationY(parentBound.centerY());
        return ObjectAnimator.ofFloat(atom, "locationX", 0, parentBound.width());
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return atom.getLocationX() > 0 && atom.getLocationX() < width;
    }
}
