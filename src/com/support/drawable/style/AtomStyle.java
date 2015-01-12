package com.support.drawable.style;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

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
}
