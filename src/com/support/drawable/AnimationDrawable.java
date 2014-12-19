package com.support.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

/**
 * Created by ivonhoe on 14-12-18.
 * Based on http://cyrilmottier.com/2012/11/27/actionbar-on-the-move/
 */
public abstract class AnimationDrawable extends Drawable implements Animatable {

    protected static final long FRAME_DURATION = 1000 / 60;

    protected boolean mIsRunning;
    protected final Paint mPaint = new Paint();

    private Callback mCallback;

    protected Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            onUpdate();

            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
            invalidateSelf();
        }
    };

    @Override
    public void start() {
        if (isRunning())
            return;

        if (mCallback != null) {
            mCallback.onStart();
        }
        mIsRunning = true;
        scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (isRunning()) {
            unscheduleSelf(mUpdater);
            mIsRunning = false;

            if (mCallback != null) {
                mCallback.onStop();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    @Override
    public abstract void draw(Canvas canvas);

    public abstract void onUpdate();

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        public void onStop();

        public void onStart();
    }
}
