package com.support.widget.progressbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.support.animation.FreePathInterpolator;
import com.support.drawable.AnimationDrawable;
import com.support.drawable.AtomDrawable;
import com.support.drawable.style.AtomFactory;
import com.support.drawable.style.AtomStyle;
import com.support.drawable.style.CircleAtomFactory;

/**
 * Created by ivonhoe on 2014/12/29.
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
    protected AnimationDrawable setupAnimationDrawable(Context context, AttributeSet attrs) {
        AtomStyle style = new AtomStyle(context, attrs);
        AnimationDrawable d = new AtomDrawable(style);

        return d;
    }
}
