package com.support.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.animation.Interpolator;
import com.support.animation.FreePathInterpolator;
import com.support.widget.listview.utils.L;

/**
 * Created by Fan.Yang on 2014/12/24.
 */
public class PointDrawable extends AnimationDrawable {

    private static int DURATION = 4000;
    private static int DELAY = 200;
    private static int SECTION_COUNT = 6;

    private int mSectionsCount = SECTION_COUNT;
    private int reachEnd = 0;

    private Interpolator mBezierInterpolator;
    private Animator[] mAnimators;
    private Point[] mAtoms;
    private AnimatorSet mAnimatorSet;

    private String sText;

    public PointDrawable() {
        mBezierInterpolator = new FreePathInterpolator(0.03f, 0.615f, 0.995f, 0.415f,
                FreePathInterpolator.BEZIER, FreePathInterpolator.DISPLACEMENT);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7);
        mPaint.setDither(false);
        mPaint.setAntiAlias(false);
        mPaint.setColor(Color.BLUE);

        initialize();
    }

    private void initialize() {
        mAnimators = new Animator[mSectionsCount];
        mAtoms = new Point[mSectionsCount];
        for (int i = 0; i < mSectionsCount; i++) {
            mAtoms[i] = new Point();
        }
        mAnimatorSet = null;
    }

    @Override
    public void onStart() {
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void setupAnimators(Rect bound) {
        if (mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
            for (int i = 0; i < mSectionsCount; i++) {
                if (!TextUtils.isEmpty(sText)) {
                    mAtoms[i].setText(sText.substring(sText.length() - i - 1, sText.length() - i));
                }
                mAtoms[i].setLocationY(bound.centerY());
                Animator animator = ObjectAnimator.ofFloat(mAtoms[i], "locationX", 0, bound.width());
                animator.setStartDelay(DELAY * (i + 1));
                mAnimators[i] = animator;
            }
            mAnimatorSet.playTogether(mAnimators);
            mAnimatorSet.setDuration(DURATION);
            mAnimatorSet.setInterpolator(mBezierInterpolator);
            mAnimatorSet.start();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        setupAnimators(mBounds);

        for (int i = 0; i < mSectionsCount; i++) {
            if (mAtoms[i].getLocationX() > 0 && (int) mAtoms[i].getLocationX() < mBounds.width()) {
                if (!TextUtils.isEmpty(sText)) {
                    canvas.drawText(mAtoms[i].getText(), mAtoms[i].getLocationX(),
                            mAtoms[i].getLocationY(), mPaint);
                } else {
                    canvas.drawPoint(mAtoms[i].getLocationX(), mAtoms[i].getLocationY(), mPaint);
                }
            } else if ((int) mAtoms[i].getLocationX() == mBounds.width()) {
                mAtoms[i].setLocationX(-1f);
                reachEnd++;
            }
        }
    }

    @Override
    public void onUpdate() {
        if (mAnimatorSet != null && !mAnimatorSet.isRunning() && reachEnd >= mSectionsCount - 1) {
            reachEnd = 0;
            mAnimatorSet.start();
        }
    }

    public String getText() {
        return sText;
    }

    public void setText(String sText) {
        this.sText = sText;
        mSectionsCount = sText.length() == 0 ? SECTION_COUNT : sText.length();
        initialize();
        mAnimatorSet = null;
    }

    class Point {
        float locationX = -1;
        float locationY = -1;
        String text;

        public float getLocationX() {
            return locationX;
        }

        public void setLocationX(float locationX) {
            this.locationX = locationX;
        }

        public float getLocationY() {
            return locationY;
        }

        public void setLocationY(float locationY) {
            this.locationY = locationY;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
