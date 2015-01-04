package com.support.animation.freepath;

public abstract class FreePathSamplers {
    private final static int SAMPLER_SIZE = 100;

    private final FreePath mPath;
    protected final float[] mSamplers;

    public FreePathSamplers(FreePath freePath) {
        mPath = freePath;
        mSamplers = buildSamplers(mPath, SAMPLER_SIZE);
    }

    protected static int searchNearestFloor(Point[] points, int start, float x) {
        int low = start;
        int mid = start;
        int high = points.length - 1;
        while (low <= high && points[low].x <= x) {
            mid = (low + high) >>> 1;
            float midVal = points[mid].x;
            if (midVal < x)
                low = mid + 1;
            else if (midVal > x)
                high = mid - 1;
            else
                return mid;
        }
        return mid;
    }

    protected static float interpolateValue(float[] values, float t) {
        int size = values.length;
        float interval = 1.f / (size - 1);

        int floorIndex = (int) ((size - 1) * t);
        int ceilIndex = Math.min(floorIndex + 1, size - 1);
        if (floorIndex == ceilIndex) {
            return values[size - 1];
        }

        float floor = floorIndex * interval;
        float ceil = floor + interval;
        return linearInterpolate(t, floor, values[floorIndex], ceil, values[ceilIndex]);
    }

    protected static float linearInterpolate(float t, float t0, float v0, float t1, float v1) {
        float factor = (t - t0) / (t1 - t0);
        return v0 + factor * (v1 - v0);
    }

    protected abstract float[] buildSamplers(FreePath path, int samplerSize);

    protected abstract String myTypeName();

    public abstract float sampleValue(float t);

    public String getTypeName() {
        return myTypeName();
    }

    public FreePath getFreePath() {
        return mPath;
    }

    public float[] getAllSamplers() {
        return mSamplers;
    }
}
