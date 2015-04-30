package com.ivonhoe.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Interpolator;
import com.ivonhoe.interpolator.BezierInterpolator;

/**
 * Created by ivonhoe on 2014/12/24.
 */
public abstract class AtomDrawable extends FancyDrawable {

    private Interpolator DEFAULT_INTERPOLATOR = new BezierInterpolator(0.03f, 0.615f, 0.995f,
            0.415f);
    private AnimatorSet mAtomsAnimatorSet;
    private Atom[] mAtoms;
    private AtomStyle mAtomStyle;

    public AtomDrawable() {
        Interpolator bezierInterpolator = DEFAULT_INTERPOLATOR;

        AtomStyle style = new AtomStyle();
        style.setInterpolator(bezierInterpolator);
        initialize(style);
    }

    public AtomDrawable(AtomStyle style) {
        initialize(style);
    }

    private void initialize(AtomStyle style) {
        mAtomStyle = style;
        int atomCount = mAtomStyle.getSectionCount();
        mAtoms = new Atom[atomCount];
        for (int i = 0; i < atomCount; i++) {
            mAtoms[i] = new Atom();
        }
        mAtomsAnimatorSet = null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStart() {
        if (mBounds != null) {
            setupAnimators(mBounds);
            mAtomsAnimatorSet.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupAnimators(final Rect bound) {
        if (mAtomStyle != null && bound != null) {
            mAtomStyle.setParentBound(bound);
            getAtomsAnimation(mAtomStyle, mAtoms);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < mAtomStyle.getSectionCount(); i++) {
            drawAtom(canvas, mAtomStyle, mAtoms[i]);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        recreateAnimator();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void recreateAnimator() {
        if (isRunning()) {
            if (mAtomsAnimatorSet != null) {
                mAtomsAnimatorSet.cancel();
            }
            mAtomsAnimatorSet = null;
            onStart();
        } else {
            mAtomsAnimatorSet = null;
            if (mBounds != null)
                setupAnimators(mBounds);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mAtomStyle.setInterpolator(interpolator);
    }

    /**
     *
     */
    public void setAtomText(String text) {
        mAtomStyle.setText(text);
        // recreate atom list
        if (text != null && text.length() != mAtoms.length) {
            mAtoms = new Atom[text.length()];
            for (int i = 0; i < mAtoms.length; i++) {
                mAtoms[i] = new Atom();
            }
        }
        recreateAnimator();
    }

    public void setAtomTextSize(float size) {
        mAtomStyle.getPaint().setTextSize(size);
    }

    public void setPaint(Paint paint) {
        mAtomStyle.setPaint(paint);
    }

    public AtomStyle getAtomStyle() {
        return mAtomStyle;
    }

    /**
     * 一个元素如何动画
     */
    public abstract ObjectAnimator getAtomAnimator(final Atom atom, final Rect bound);

    /**
     * 一个元素是否在动画中
     */
    public abstract boolean isAtomRunning(Atom atom);

    /**
     * 根据元素样式得到动画集合
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AnimatorSet getAtomsAnimation(AtomStyle atomStyle, Atom[] atoms) {
        int count = atomStyle.getSectionCount();
        Animator[] animators = new Animator[count];
        String text = atomStyle.getText();
        final Rect bound = atomStyle.getParentBound();
        if (bound == null || mAtomsAnimatorSet != null) {
            return mAtomsAnimatorSet;
        }

        mAtomsAnimatorSet = new AnimatorSet();
        for (int i = 0; i < count; i++) {
            final Atom atom = atoms[i];
            if (!TextUtils.isEmpty(text)) {
                atom.setText(text.substring(text.length() - i - 1, text.length() - i));
            }
            ObjectAnimator animator = getAtomAnimator(atoms[i], bound);

            if (animator != null) {
                animator.setStartDelay(atomStyle.getDelay() * (i));
                animators[i] = animator;
            }
        }

        mAtomsAnimatorSet.setDuration(atomStyle.getDuration());
        mAtomsAnimatorSet.playTogether(animators);
        mAtomsAnimatorSet.setInterpolator(atomStyle.getInterpolator());
        mAtomsAnimatorSet.setStartDelay(atomStyle.getDelay());
        mAtomsAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animation.start();
            }
        });
        return mAtomsAnimatorSet;
    }

    /**
     * 根据每个元素的状态和样式绘制
     */
    public void drawAtom(Canvas canvas, AtomStyle style, Atom atom) {
        if (!isAtomRunning(atom)) {
            return;
        }
        Paint paint = style.getPaint();
        AtomStyle.Shape shape = style.getShape();

        switch (shape) {
            case CHAR:
                canvas.drawText(atom.getText(), atom.getLocationX(), atom.getLocationY(), paint);
                break;

            case POINT:
                canvas.drawPoint(atom.getLocationX(), atom.getLocationY(), paint);
                break;

            case CIRCLE:
                canvas.drawCircle(atom.getLocationX(), atom.getLocationY(), 5, paint);
                break;
        }
    }

    public class Atom {
        private float delta = 0;
        private float locationX = -1;
        private float locationY = -1;
        private String text;

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

        public float getDelta() {
            return delta;
        }

        public void setDelta(float delta) {
            this.delta = delta;
        }
    }
}
