package com.support.animation.freepath;

public class Point {
    public float x;
    public float y;

    public Point() {

    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
}

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point sub(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point mul(Point vec, float scalar) {
        return new Point(vec.x * scalar, vec.y * scalar);
    }
}
