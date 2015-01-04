package com.support.animation.freepath;

import java.util.List;

public abstract class FreePath {
    private Point[] mCtrlPoints;

    public void setControlPoints(Point... points) {
        mCtrlPoints = points;
    }

    public void setControlPoints(List<Point> points) {
        mCtrlPoints = new Point[points.size()];
        points.toArray(mCtrlPoints);
    }

    public Point[] getControlPoints() {
        return mCtrlPoints == null ? new Point[0] : mCtrlPoints;
    }

    public abstract Point getPathPoint(float phase);
}
