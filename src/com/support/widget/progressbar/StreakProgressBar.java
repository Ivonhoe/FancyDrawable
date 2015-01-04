package com.support.widget.progressbar;

import android.content.Context;
import android.util.AttributeSet;
import com.support.drawable.AnimationDrawable;
import com.support.drawable.StreakDrawable;

/**
 * Created by Fan.Yang on 2014/12/29.
 */
public class StreakProgressBar extends SmoothProgressBar{
    public StreakProgressBar(Context context) {
        super(context);
    }

    public StreakProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StreakProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupAnimationDrawable() {
        AnimationDrawable d = new StreakDrawable();
        setAnimationDrawable(d);
    }
}
