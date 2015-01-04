package com.support.animation.freepath;

import android.os.SystemClock;
import android.util.Log;

public class FreePathSamplersDisplacement extends FreePathSamplers {
    private static final String TYPE_NAME = "FreePathSamplersDisplacement";

    public FreePathSamplersDisplacement(FreePath freePath) {
        super(freePath);
    }

    @Override
    public float sampleValue(float t) {
        float t1 = t < 0 ? 0 : t;
        t1 = t1 > 1 ? 1 : t;

        return interpolateValue(mSamplers, t1);
    }

    @Override
    protected float[] buildSamplers(FreePath path, int samplerSize) {
        float[] displacementSamplers = new float[samplerSize];

        int superSamplerSize = 4 * samplerSize;
        int samplerSizeMinusOne = samplerSize - 1;
        int superSamplerSizeMinusOne = superSamplerSize - 1;

        Long a = SystemClock.uptimeMillis();

        Point[] superSamplers = new Point[superSamplerSize];
        for (int i = 0; i < superSamplerSize; i++) {
            superSamplers[i] = path.getPathPoint(i / (float) superSamplerSizeMinusOne);
        }

        Long b = SystemClock.uptimeMillis();

        int prevFloorIndex = 0;
        for (int i = 0; i < samplerSize; i++) {
            float t = i / (float) samplerSizeMinusOne;

            int floorIndex = searchNearestFloor(superSamplers, prevFloorIndex, t);
            int ceilIndex = Math.min(floorIndex + 1, superSamplerSizeMinusOne);

            float t0 = superSamplers[floorIndex].x;
            float t1 = superSamplers[ceilIndex].x;

            float d0 = superSamplers[floorIndex].y;
            float d1 = superSamplers[ceilIndex].y;

            if (floorIndex != ceilIndex && t0 != t1) {
                displacementSamplers[i] = linearInterpolate(t, t0, d0, t1, d1);
            } else {
                displacementSamplers[i] = d0;
            }

            prevFloorIndex = floorIndex;
        }

        return displacementSamplers;
    }

    @Override
    protected String myTypeName() {
        return TYPE_NAME;
    }
}
