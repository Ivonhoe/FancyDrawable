package com.support.drawable;

import android.graphics.*;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.*;
import android.view.animation.Interpolator;
import com.support.view.widget.utils.L;

/**
 * Created by ivonhoe on 14-12-15.
 */
public class ProgressDrawable extends AnimationDrawable {

    private static final float OFFSET_PER_FRAME = 0.01f;

    private Rect mBounds;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mSectionsCount = 6;
    private int mSeparatorWidth = 10;
    private int mStrokeWidth = 9;
    private float mSpeed = 0.5f;
    private float mCurrentOffset;
    private float mMaxOffset = 1f / mSectionsCount;

    private float xSectionWidth = 1f / mSectionsCount;
    private int boundWidth;
    private int boundHeight;
    private int boundCenterX;
    private int boundCenterY;

    public ProgressDrawable() {

        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(false);
        mPaint.setAntiAlias(false);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void draw(Canvas canvas) {
        mBounds = getBounds();
        boundHeight = mBounds.height();
        boundWidth = mBounds.width();
        boundCenterX = mBounds.centerX();
        boundCenterY = mBounds.centerY();
        canvas.clipRect(mBounds);

        canvas.drawLine(mBounds.left, boundCenterY - 20, mBounds.right,
                boundCenterY - 20, mPaint);
        drawStrokes(canvas);

        drawPoint(canvas);
    }

    @Override
    public void onUpdate() {
        mCurrentOffset += (OFFSET_PER_FRAME * mSpeed);

        if (mCurrentOffset >= mMaxOffset) {
            // mNewTurn = true;
            mCurrentOffset -= mMaxOffset;
        }
    }

    private void drawStrokes(Canvas canvas) {
        int prev;
        int end;
        int spaceLength;
        for (int i = 0; i <= mSectionsCount; i++) {
            float xOffset = i * xSectionWidth + mCurrentOffset;
            xOffset = Math.max(0f, xOffset - xSectionWidth);
            prev = (int) (mInterpolator.getInterpolation(xOffset) * boundWidth);

            float ratioSectionWidth =
                    Math.abs(mInterpolator.getInterpolation(xOffset) -
                            mInterpolator.getInterpolation(Math.min(xOffset + xSectionWidth, 1f)));

            //separator between each piece of line
            int sectionWidth = (int) (boundWidth * ratioSectionWidth);
            if (sectionWidth + prev < boundWidth)
                spaceLength = Math.min(sectionWidth, mSeparatorWidth);
            else
                spaceLength = 0;
            int drawLength = sectionWidth > spaceLength ? sectionWidth - spaceLength : 0;
            end = prev + drawLength;

            // L.d("i=" + i + ",pre=" + prev + ",end=" + end +",width="+boundWidth);
            if (end > prev) {
                canvas.drawLine(prev, boundCenterY, end, boundCenterY, mPaint);
            }

            if (i == mSectionsCount && end < boundWidth - mSeparatorWidth) {
                canvas.drawLine(end, boundCenterY, boundWidth, boundCenterY, mPaint);
            }
        }
    }

    float mDuration = 4;
    float mDelay = 0.05f;

    private void drawPoint(Canvas canvas) {
        int locationX;
        for (int i = mSectionsCount; i >= 0; i--) {
            float xOffset = i * xSectionWidth + mCurrentOffset;
            xOffset = Math.max(0f, xOffset - xSectionWidth);

            locationX = (int) (mInterpolator.getInterpolation(xOffset) * boundWidth / 2);
            canvas.drawPoint(locationX, boundCenterY + 20, mPaint);
        }
    }
}
