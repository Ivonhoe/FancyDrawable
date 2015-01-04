package com.support.animation.freepath;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CatmullRomPath extends FreePath {
    private final List<BezierPath> mSegmentBezierPaths = new ArrayList<BezierPath>();

    private static BezierPath createBezierPath(Point p0, Point p1, Point p2, Point p3) {
        float vecFactor = 1 / 6.f;
        Point l1 = Point.mul(Point.sub(p2, p0), vecFactor);
        Point l2 = Point.mul(Point.sub(p3, p1), vecFactor);

        Point q1 = Point.add(p1, l1);
        Point q2 = Point.sub(p2, l2);

        BezierPath bezierPath = new BezierPath();
        bezierPath.setControlPoints(p1, q1, q2, p2);

        return bezierPath;
    }

    @Override
    public void setControlPoints(Point... points) {
        super.setControlPoints(points);
        rebuildSegmentBezierPaths();
    }

    @Override
    public void setControlPoints(List<Point> points) {
        super.setControlPoints(points);
        rebuildSegmentBezierPaths();
    }

    @Override
    public Point getPathPoint(float phase) {
        int size = mSegmentBezierPaths.size();

        if (size < 1) {
            return new Point(0, 0);
        }

        float scaledPhase = phase * size;
        int index = (int) Math.min(Math.floor(scaledPhase), size - 1);
        float subPhase = scaledPhase - index;

        Point p = mSegmentBezierPaths.get(index).getPathPoint(subPhase);
        return p;
    }

    private void rebuildSegmentBezierPaths() {
        mSegmentBezierPaths.clear();

        final Point[] ctrlPoints = getControlPoints();

        List<Point> newPoints = new ArrayList<Point>();
        newPoints.add(ctrlPoints[0]);
        for (int i = 0; i < ctrlPoints.length; i++) {
            newPoints.add(ctrlPoints[i]);
        }
        newPoints.add(ctrlPoints[ctrlPoints.length - 1]);

        for (int i = 0; i < newPoints.size(); i++) {
            buildSegmentBezierPath(newPoints, i);
        }
    }

    private void buildSegmentBezierPath(List<Point> points, int index) {
        if (points.size() < 3 || index < 3) {
            return;
        }

        Point p0 = points.get(index - 3);
        Point p1 = points.get(index - 2);
        Point p2 = points.get(index - 1);
        Point p3 = points.get(index);

        mSegmentBezierPaths.add(createBezierPath(p0, p1, p2, p3));
    }
}
