package com.support.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.support.animation.freepath.*;
import com.support.widget.listview.utils.L;
import org.ivonhoe.supportlib.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FreePathInterpolator implements Interpolator {
    public static final int VELOCITY = 1;
    public static final int DISPLACEMENT = 0;

    public static final int CATMULL_ROM = 1;
    public static final int BEZIER = 0;

    private int pathType;
    private int usage;
    private FreePathSamplers mFreePathSamplers;

    public FreePathInterpolator(float x0, float y0, float x1, float y1, int pathType, int samplerType) {
        List<Point> controls = new ArrayList<Point>();
        controls.add(new Point(0f, 0f));
        controls.add(new Point(x0, y0));
        controls.add(new Point(x1, y1));
        controls.add(new Point(1f, 1f));
        mFreePathSamplers = createFreePathSampler(pathType, samplerType, controls);
    }

    public FreePathInterpolator(int pathType, int samplerType, List<Point> points) {
        mFreePathSamplers = createFreePathSampler(pathType, samplerType, points);
    }

    public FreePathInterpolator(FreePathSamplers samplers) {
        mFreePathSamplers = samplers;
    }

    public FreePathInterpolator(Context context, XmlPullParser parser, AttributeSet attrs)
            throws IOException, XmlPullParserException {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FreePathInterpolator);
        pathType = a.getInt((R.styleable.FreePathInterpolator_pathType), CATMULL_ROM);
        usage = a.getInt((R.styleable.FreePathInterpolator_pathUsage), DISPLACEMENT);
        a.recycle();

        List<Point> controlPoints = new ArrayList<Point>();

        int type;
        int depth = parser.getDepth();
        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {
            if ((type != XmlPullParser.START_TAG)) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("anchor")) {
                TypedArray ap = context.obtainStyledAttributes(attrs, R.styleable.Anchor);
                if (ap.hasValue(R.styleable.Anchor_xCoord) && ap.hasValue(R.styleable.Anchor_yCoord)) {
                    float x = ap.getFloat(R.styleable.Anchor_xCoord, 0f);
                    float y = ap.getFloat(R.styleable.Anchor_yCoord, 0f);
                    Point nail = new Point(x, y);
                    controlPoints.add(nail);
                }
                ap.recycle();
            }
        }

        mFreePathSamplers = createFreePathSampler(pathType, usage, controlPoints);
    }

    private static FreePathSamplers createFreePathSampler(int pathType, int samplerType, List<Point> points) {
        FreePath freePath;
        switch (pathType) {

            case BEZIER:
                freePath = new BezierPath();
                break;
            case CATMULL_ROM:
                freePath = new CatmullRomPath();
                break;
            default:
                freePath = new CatmullRomPath();
        }
        freePath.setControlPoints(points);

        FreePathSamplers samplers;
        switch (samplerType) {
            case DISPLACEMENT:
                samplers = new FreePathSamplersDisplacement(freePath);
                break;
            case VELOCITY:
                samplers = new FreePathSamplersVelocity(freePath);
                break;
            default:
                samplers = new FreePathSamplersDisplacement(freePath);
                break;
        }
        return samplers;
    }

    @Override
    public float getInterpolation(float input) {
        if (mFreePathSamplers == null) {
            return input;
        }

        return mFreePathSamplers.sampleValue(input);
    }
}
