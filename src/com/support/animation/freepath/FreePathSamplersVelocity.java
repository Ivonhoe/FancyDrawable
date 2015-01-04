package com.support.animation.freepath;

public class FreePathSamplersVelocity extends FreePathSamplers {
    private final static String TYPE_NAME = "FreePathSamplersVelocity";

    public FreePathSamplersVelocity(FreePath freePath) {
        super(freePath);
    }

    @Override
    public float sampleValue(float t) {
        float t1 = t < 0 ? 0 : t;
        t1 = t1 > 1 ? 1 : t;

        return interpolateValue(mSamplers, t1) / mSamplers[mSamplers.length - 1];
    }

    @Override
    protected float[] buildSamplers(FreePath path, int samplerSize) {
        FreePathSamplersDisplacement firstPowerSamplers = new FreePathSamplersDisplacement(path);
        final float[] velocitySamplers = firstPowerSamplers.getAllSamplers();

        float[] displacementSamplers = new float[samplerSize];
        int samplerSizeMinusOne = samplerSize - 1;

        float interval = 1.f / samplerSizeMinusOne;
        float integration = 0;
        for (int i = 0; i < samplerSize; i++) {
            displacementSamplers[i] = integration;

            float v = interpolateValue(velocitySamplers, (i + 0.5f) * interval);
            integration += v * interval;
        }

        return displacementSamplers;
    }

    @Override
    protected String myTypeName() {
        return TYPE_NAME;
    }
}

