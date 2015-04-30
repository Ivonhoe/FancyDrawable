package com.ivonhoe.drawable;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

/**
 * Created by ivonhoe on 15-4-30.
 */
public class BallsLineDrawable extends AtomDrawable {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public ObjectAnimator getAtomAnimator(Atom atom, Rect bound) {
        if (atom == null || bound == null)
            return null;
        AtomStyle style = getAtomStyle();
        Paint paint = style.getPaint();
        AtomStyle.Shape shape = style.getShape();
        if (shape == AtomStyle.Shape.CHAR) {
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = mBounds.top +
                    (mBounds.bottom - mBounds.top - fontMetrics.bottom + fontMetrics.top) / 2 -
                    fontMetrics.top;
            paint.setTextAlign(Paint.Align.CENTER);
            atom.setLocationY(baseline);
        } else {
            atom.setLocationY(bound.centerY());
        }

        return ObjectAnimator.ofFloat(atom, "locationX", 0, bound.width());
    }

    @Override
    public boolean isAtomRunning(Atom atom) {
        return atom.getLocationX() > 0 && atom.getLocationX() < mBounds.width();
    }
}
