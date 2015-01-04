package com.support.drawable;

import android.graphics.*;
import android.os.SystemClock;
import android.view.animation.AnimationUtils;
import com.support.widget.listview.utils.L;

import java.util.Random;

/**
 * Created by ivonhoe on 14-12-18.
 */
public class ColorDrawable extends AnimationDrawable {

    private static final long ANIMATION_DURATION = 1500;

    private static final int ACCCENT_COLOR = 0x33FFFFFF;
    private static final int DIM_COLOR = 0x33000000;

    private static final Random mRandom = new Random();

    private int mStartColor;
    private int mEndColor;
    private int mCurrentColor;

    private long mStartTime;

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
    public void onStart() {
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartColor = randomColor();
        mEndColor = randomColor();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setColor(mCurrentColor);
        canvas.drawRect(mBounds, mPaint);

        mPaint.setColor(ACCCENT_COLOR);
        canvas.drawRect(mBounds.left, mBounds.top, mBounds.right, mBounds.top + 1, mPaint);

        mPaint.setColor(DIM_COLOR);
        canvas.drawRect(mBounds.left, mBounds.bottom - 2, mBounds.right, mBounds.bottom, mPaint);
    }

    private static int randomColor() {
        return mRandom.nextInt() & 0x00FFFFFF;
    }

    private static int evaluate(float fraction, int startValue, int endValue) {
        return (int) (startValue + fraction * (endValue - startValue));
    }
}
