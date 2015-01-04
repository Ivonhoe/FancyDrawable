package com.support.animation.freepath;

import java.util.Arrays;
import java.util.List;

public class BezierPath extends FreePath {
    @Override
    public Point getPathPoint(float phase) {
        final Point[] ctrlPoints = getControlPoints();
        final int size = ctrlPoints.length;

        if (size < 1) {
            return new Point(0, 0);
        }

        Point[] calcPoints = new Point[size];
        for (int i = 0; i < size; i++) {
            calcPoints[i] = new Point(ctrlPoints[i]);
        }

        return deCasteljau2(calcPoints, phase);
    }

    private float deCasteljau(float[] values, float phase) {
        for (int j = values.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                values[i] = values[i] * (1 - phase) + values[i + 1] * phase;
            }
        }
        return values[0];
    }

    private Point deCasteljau2(Point[] points, float phase) {
        Point nextPoint = new Point();

        for (int j = points.length - 1; j > 0; j--) {
            points[0].x *= (1 - phase);
            points[0].y *= (1 - phase);

            for (int i = 0; i < j; i++) {
                nextPoint.x = points[i + 1].x * phase;
                nextPoint.y = points[i + 1].y * phase;

                points[i + 1].x -= nextPoint.x;
                points[i + 1].y -= nextPoint.y;

                points[i].x += nextPoint.x;
                points[i].y += nextPoint.y;
            }
        }

        return points[0];
    }
}
