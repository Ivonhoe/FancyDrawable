package com.support.drawable.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import org.ivonhoe.supportlib.R;

/**
 * Created by ivonhoe on 2015/1/5.
 */
public class AtomStyle {

    private static int DURATION = 4000;
    private static int DELAY = 200;
    private static int SECTION_COUNT = 6;
    private static int PADDING = 10;

    public enum Shape {POINT, CHAR, CIRCLE}

    Rect parentBound;

    // draw a point , a char, or circle?
    Shape shape;

    Interpolator interpolator;

    int duration;

    int delay;

    int sectionCount;

    Paint paint;

    String text;

    AtomFactory atomFactory;

    public AtomStyle(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Atom);
        duration = a.getInt(R.styleable.Atom_duration, DURATION);
        delay = a.getInt(R.styleable.Atom_delay, DELAY);
        text = a.getString(R.styleable.Atom_text);
        sectionCount = text == null ? a.getInt(R.styleable.Atom_count, SECTION_COUNT) : text.length();
        interpolator = AtomUtils.loadInterpolator(context, a.getResourceId(
                R.styleable.Atom_interpolator, R.anim.linear_interpolator));

        paint = new Paint();
        paint.setDither(false);
        paint.setAntiAlias(false);
        int s = text == null ? a.getInt(R.styleable.Atom_shape, 0) : 1;
        switch (s) {
            case 0:
                shape = Shape.POINT;
                paint.setStyle(Paint.Style.STROKE);
                break;
            case 1:
                shape = Shape.CHAR;
                break;
            case 2:
                shape = Shape.CIRCLE;
                break;
        }

        int f = a.getInt(R.styleable.Atom_factory, 0);
        switch (f) {
            case 0:
                atomFactory = new HorizontalAtomFactory();
                break;
            case 1:
                atomFactory = new CircleAtomFactory();
                break;
        }

        int c = a.getColor(R.styleable.Atom_android_textColor, Color.BLUE);
        float size = a.getDimension(R.styleable.Atom_textSize, 36f);
        int width = a.getInt(R.styleable.Atom_strokeWidth, 7);

        paint.setStrokeWidth(width);
        paint.setColor(c);
        paint.setTextSize(size);
    }

    public AtomStyle() {
        this.shape = Shape.POINT;
        this.interpolator = new LinearInterpolator();
        this.duration = DURATION;
        this.delay = DELAY;
        this.sectionCount = SECTION_COUNT;

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(7);
        this.paint.setDither(false);
        this.paint.setAntiAlias(false);
        this.paint.setColor(Color.BLUE);

        this.atomFactory = new HorizontalAtomFactory();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.shape = Shape.CHAR;
    }

    public Rect getParentBound() {
        return parentBound;
    }

    public void setParentBound(Rect parentBound) {
        int top = parentBound.top + PADDING;
        int bottom = parentBound.bottom - PADDING;
        this.parentBound = new Rect(parentBound.left, top, parentBound.right, bottom);
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape type) {
        this.shape = type;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(int count) {
        this.sectionCount = count;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public AtomFactory getAtomFactory() {
        return atomFactory;
    }

    public void setAtomFactory(AtomFactory atomFactory) {
        this.atomFactory = atomFactory;
    }
}
