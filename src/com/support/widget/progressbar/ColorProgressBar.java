package com.support.widget.progressbar;

import android.content.Context;
import android.util.AttributeSet;
import com.support.drawable.AnimationDrawable;
import com.support.drawable.ColorDrawable;

/**
 * Created by Ivonhoe on 2014/12/29.
 */
public class ColorProgressBar extends SmoothProgressBar {
    public ColorProgressBar(Context context) {
        super(context);
    }

    public ColorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected AnimationDrawable setupAnimationDrawable(Context context, AttributeSet attrs) {
        AnimationDrawable d = new ColorDrawable();

        return d;
    }
}
