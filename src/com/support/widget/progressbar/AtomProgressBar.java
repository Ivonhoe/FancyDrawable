package com.support.widget.progressbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.support.animation.FreePathInterpolator;
import com.support.drawable.AnimationDrawable;
import com.support.drawable.AtomDrawable;
import com.support.drawable.style.AtomFactory;
import com.support.drawable.style.AtomStyle;
import com.support.drawable.style.CircleAtomFactory;

/**
 * Created by Ivonhoe on 2014/12/29.
 */
public class AtomProgressBar extends SmoothProgressBar {

    public AtomProgressBar(Context context) {
        super(context);
    }

    public AtomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AtomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupAnimationDrawable() {
        AtomStyle style = new AtomStyle();
        Interpolator bezierInterpolator = new FreePathInterpolator(0.1f, 0.68f, 0.23f, 0.86f,
                FreePathInterpolator.BEZIER, FreePathInterpolator.DISPLACEMENT);
        style.setInterpolator(bezierInterpolator);
        AtomFactory factory = new CircleAtomFactory();
        AnimationDrawable d = new AtomDrawable(style, factory);

        Paint paint = new Paint();
        paint.setTextSize(25);
        paint.setStrokeWidth(1);
        paint.setDither(false);
        paint.setAntiAlias(false);
        paint.setColor(Color.BLUE);

        setAnimationDrawable(d);
    }
}
