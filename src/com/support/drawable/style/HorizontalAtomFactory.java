package com.support.drawable.style;

import android.animation.ObjectAnimator;
import android.graphics.Rect;

/**
 * Created by Fan.Yang on 2015/1/7.
 */
public class HorizontalAtomFactory extends AtomFactory {

    @Override
    public ObjectAnimator getAtomAnimator(Atom atom, Rect parentBound) {
        if (atom == null || parentBound == null)
            return null;

        atom.setLocationY(parentBound.centerY());
        return ObjectAnimator.ofFloat(atom, "locationX", 0, parentBound.width());
    }
}
