package com.support.widget.progressbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import com.support.drawable.AnimationDrawable;
import com.support.drawable.PointDrawable;

/**
 * Created by Fan.Yang on 2014/12/29.
 */
public class PointProgressBar extends SmoothProgressBar{
    public PointProgressBar(Context context) {
        super(context);
    }

    public PointProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PointProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupAnimationDrawable() {
        AnimationDrawable d = new PointDrawable();

        ((PointDrawable) d).setText("LOADING");
        Paint paint = new Paint();
        paint.setTextSize(25);
        paint.setStrokeWidth(1);
        paint.setDither(false);
        paint.setAntiAlias(false);
        paint.setColor(Color.BLUE);
        ((PointDrawable) d).setPaint(paint);
        setAnimationDrawable(d);
    }
}
