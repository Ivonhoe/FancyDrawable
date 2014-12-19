package com.support.drawable;

import android.graphics.*;
import android.os.SystemClock;
import android.view.animation.AnimationUtils;

import java.util.Random;

/**
 * Created by ivonhoe on 14-12-18.
 */
public class ColorAnimationDrawable extends AnimationDrawable {

    private static final long ANIMATION_DURATION = 1500;

    private static final int ACCCENT_COLOR = 0x33FFFFFF;
    private static final int DIM_COLOR = 0x33000000;

    private static final Random mRandom = new Random();

    private int mStartColor;
    private int mEndColor;
    private int mCurrentColor;

    private long mStartTime;

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();

        mPaint.setColor(mCurrentColor);
        canvas.drawRect(bounds, mPaint);

        mPaint.setColor(ACCCENT_COLOR);
        canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.top + 1, mPaint);

        mPaint.setColor(DIM_COLOR);
        canvas.drawRect(bounds.left, bounds.bottom - 2, bounds.right, bounds.bottom, mPaint);
    }

    @Override
    public void onUpdate() {
        long now = AnimationUtils.currentAnimationTimeMillis();
        long duration = now - mStartTime;
        if (duration >= ANIMATION_DURATION) {
            mStartColor = mEndColor;
            mEndColor = randomColor();
            mStartTime = now;
            mCurrentColor = mStartColor;
        } else {
            float fraction = duration / (float) ANIMATION_DURATION;
            //@formatter:off
            mCurrentColor = Color.rgb(
                    evaluate(fraction, Color.red(mStartColor), Color.red(mEndColor)),     // red
                    evaluate(fraction, Color.green(mStartColor), Color.green(mEndColor)),
                    // green
                    evaluate(fraction, Color.blue(mStartColor), Color.blue(mEndColor)));  // blue
            //@formatter:on
        }
    }

    @Override
    public void start() {
        if (!isRunning()) {
            mIsRunning = true;

            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            mStartColor = randomColor();
            mEndColor = randomColor();

            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
            invalidateSelf();
        }
    }

    private static int randomColor() {
        return mRandom.nextInt() & 0x00FFFFFF;
    }

    private static int evaluate(float fraction, int startValue, int endValue) {
        return (int) (startValue + fraction * (endValue - startValue));
    }
}
