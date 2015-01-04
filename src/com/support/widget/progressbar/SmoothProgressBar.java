package com.support.widget.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.support.drawable.AnimationDrawable;
import com.support.drawable.StreakDrawable;

/**
 * Created by ivonhoe on 14-12-11.
 */
public abstract class SmoothProgressBar extends ProgressBar {

    public SmoothProgressBar(Context context) {
        super(context);
    }

    public SmoothProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupAnimationDrawable();
    }

    public SmoothProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isIndeterminate() && getIndeterminateDrawable() instanceof AnimationDrawable) {
            getIndeterminateDrawable().draw(canvas);
        }
    }

    public void setAnimationDrawable(AnimationDrawable animationDrawable){
        setIndeterminateDrawable(animationDrawable);
    }

    private AnimationDrawable checkIndeterminateDrawable() {
        Drawable drawable = getIndeterminateDrawable();
        if (drawable == null || !(drawable instanceof AnimationDrawable)) {
            throw new RuntimeException("The drawable is not a ProgressDrawable");
        }
        return (AnimationDrawable) drawable;
    }

    public void progressiveStart() {
        checkIndeterminateDrawable().start();
    }

    public void progressiveStop() {
        checkIndeterminateDrawable().stop();
    }

    protected abstract void setupAnimationDrawable();
}
